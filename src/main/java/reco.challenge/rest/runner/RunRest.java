package reco.challenge.rest.runner;

import org.eclipse.jetty.server.Server;
import reco.challenge.rest.server.RestServer;

public class RunRest {
    public static void main(String[] args) throws Exception {
        Server server = RestServer.createServer(8091);
        server.start();
        server.join();
    }
}
