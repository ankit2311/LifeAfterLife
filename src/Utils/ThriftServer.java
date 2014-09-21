package Utils;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServlet;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import sos.emergency_helper;

/**
 * @author: Kumar Ankit
 */
public class ThriftServer {
    private void start() {
        try {
            TServerSocket serverTransport = new TServerSocket(7911);

            emergency_helper.Processor processor = new emergency_helper.Processor(new Server());

            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).
                    processor(processor));
            System.out.println("Starting server on port 7911 ...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ThriftServer srv = new ThriftServer();
        srv.start();
    }
}
