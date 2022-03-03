package reco.challenge.persist;

import org.apache.spark.sql.Dataset;
import org.elasticsearch.spark.sql.api.java.JavaEsSparkSQL;

public class ToElastic {
    public static void persist(Dataset gDS){

        JavaEsSparkSQL.saveToEs(gDS, "reco_challenge/product_tuple_counts");

    }
}
