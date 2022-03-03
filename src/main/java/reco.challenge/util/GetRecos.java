package reco.challenge.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GetRecos {

    public static List<String> get(String gProductId) {

        List<String> retVal = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/postgres";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "postgres");

            Connection conn = DriverManager.getConnection(url, props);

            PreparedStatement pStmt = conn.prepareStatement("select count, product1, product2 from ep_letsdoit.product_tuple_counts where product1=? or product2=? order by count desc");

            pStmt.setString(1, gProductId);
            pStmt.setString(2, gProductId);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                retVal.add(rs.getString(2));
                retVal.add(rs.getString(3));
            }

            rs.close();
            pStmt.close();
            conn.close();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
        catch(SQLException sqlEx) {
            sqlEx.printStackTrace();
            return null;
        }
        finally{
            return retVal;
        }
    }
}
