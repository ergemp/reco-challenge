package reco.challenge.rest.servlet;

import reco.challenge.rest.response.Response200;
import reco.challenge.rest.response.Response500;
import reco.challenge.util.GetRecos;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Reco extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter prt = response.getWriter();
        BufferedReader rdr = request.getReader();

        String readLine;
        StringBuffer sb = new StringBuffer();

        while ((readLine = rdr.readLine()) != null){
            sb.append(readLine);
        }

        try {
            //get auths with groupName
            if (request.getParameter("productId") != null) {

                List<String> productRecos = GetRecos.get(request.getParameter("productId").toString());

                //prt.println("{\"data\":" + jsonArray + "}");
                prt.println( productRecos.toString() );

            }
            //else return 500
            else {
                response = Response500.set500(response);
            }

            response = Response200.set200(response);

        } catch (Exception ex ) {
            response = Response500.set500(response);
            ex.printStackTrace();
        }
    }
}
