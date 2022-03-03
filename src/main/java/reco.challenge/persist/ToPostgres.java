package reco.challenge.persist;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;

import java.io.Serializable;
import java.util.Properties;

public class ToPostgres implements Serializable {
    public static void persist(Dataset gDS){
        Properties cnnProps = new Properties();
        cnnProps.setProperty("driver", "org.postgresql.Driver");
        cnnProps.setProperty("user", "postgres");
        cnnProps.setProperty("password", "postgres");

        gDS.write().mode(SaveMode.Overwrite).jdbc("jdbc:postgresql://localhost/postgres","reco_challenge.product_tuple_counts", cnnProps);
    }
}
