package reco.challenge.rest.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import reco.challenge.rest.servlet.Reco;

public class RestServer {
    public static org.eclipse.jetty.server.Server createServer(int port) {
        Server server = new Server(port);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(Reco.class, "/rest/reco");


        return server;
    }
}
