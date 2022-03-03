package reco.challenge.runner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import reco.challenge.function.TupleListConverter;
import reco.challenge.model.ProductTupleCountRow;
import reco.challenge.model.SummaryRow;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class run01 {

    public static void main(String[] args) {
        Logger.getLogger("org").setLevel(Level.ERROR);
        Logger.getLogger("akka").setLevel(Level.OFF);

        // configure spark
        SparkSession spark = SparkSession
                .builder()
                .appName("reco.challenge.run01")
                .master("local")
                //.config("es.index.auto.create", "true")
                //.config("es.nodes", "localhost")
                //.config("es.port", "9200")
                .getOrCreate();

        //open the local csv file
        String csvPath = "resources/sample.csv";
        JavaRDD<Row> rawFile = spark
                .read()
                //.format("csv")  //multiple sources found for csv
                .format("org.apache.spark.sql.execution.datasources.csv.CSVFileFormat")
                .option("delimiter","\t")
                .option("header", "false")
                .option("mode", "DROPMALFORMED")
                .load(csvPath)
                .toJavaRDD()
                ;

        JavaPairRDD<String, Iterable<String>> groupedProducts = rawFile.filter(k -> {
                    // file needs data cleansing
                    if (k.get(19) != null &&
                        !k.get(19).toString().equalsIgnoreCase("\\N") &&
                        !k.get(19).toString().trim().equalsIgnoreCase("")) {
                        return true;
                    }
                    else {
                        return false;
                    }
                })
                .flatMap(k -> {

                    String productIds = k.get(19).toString().replaceAll("[\\[\\]\\\"]","");
                    List<SummaryRow> rows = new ArrayList<>();

                    // flatmapping productsIds for each sessionId
                    if (productIds.contains(",")){
                        for (String productId : Arrays.asList(productIds.split(","))) {
                            SummaryRow summaryRow = new SummaryRow();
                            rows.add(new SummaryRow(k.get(13).toString(), k.get(14).toString(), productId));
                        }
                    }
                    else {
                        rows.add(new SummaryRow(k.get(13).toString(), k.get(14).toString(), k.get(19).toString()));
                    }

                    return rows.iterator();

                })
                // now pairing sessionIds and all the products of the session viewed
                .mapToPair(r -> new Tuple2<>(r.getSessionId(), r.getProductIds()))
                .groupByKey()
                ;

        //groupedProducts.foreach(k -> System.out.println(k));

        // output example should be like following
        // (867d8545-98d4-d1ea-5132-ad3f89515855_1482902950,[488328, 469310, 476227, 473894, 488328, 472026, 482711, 475585, 482711])

        //System.out.println(groupedProducts.count()); //matches with the SQL equilavent, 1123

        // now calculating the paired products seen together within each session
        // this data will be used to recommend products
        JavaPairRDD totalBinaryProductCounts = groupedProducts.map(new TupleListConverter())
                .filter(line -> line.size() > 0)
                .flatMap(line -> line.iterator())
                .mapToPair(line -> new Tuple2<>(line,1))
                .reduceByKey((x, y) -> x + y)
                .mapToPair(line -> new Tuple2(line._2, line._1))
                .sortByKey()
                ;

        //System.out.println(totalBinaryProductCounts.count()); //14581 total binary tuples
        //totalBinaryProductCounts.foreach(line -> System.out.println(line.toString()));

        // count the paired products which have seen together
        JavaRDD<ProductTupleCountRow>  finalRDD = totalBinaryProductCounts.map(new Function<Tuple2<Integer,String>, ProductTupleCountRow>() {
            @Override
            public ProductTupleCountRow call(Tuple2<Integer, String> tuple) {
                return new ProductTupleCountRow(tuple._1, tuple._2.split(",")[0], tuple._2.split(",")[1]);
            }
        });

        Dataset<ProductTupleCountRow> finalDS =  spark.createDataset(finalRDD.rdd(), Encoders.bean(ProductTupleCountRow.class));
        finalDS.printSchema();

        finalDS.createOrReplaceTempView("finalTable");
        spark.sql("select * from finalTable where count > 1").show();

        // persist the data
        // so can be used from the rest service

        //ToPostgres.persist(finalDS);
        //ToElastic.persist(finalDS);
    }
}
