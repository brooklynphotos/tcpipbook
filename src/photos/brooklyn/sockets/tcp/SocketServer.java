package photos.brooklyn.sockets.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private final int port;

    public SocketServer(String[] args) throws IOException {
        if(args.length>0){
            this.port = Integer.parseInt(args[0]);
        }else{
            this.port = 4001;
        }
        startServer();
    }

    private void startServer() throws IOException {
        try(final ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Starting connection....");
            while (true) {
                try(
                    final Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ){
                    System.out.println("Started connection on "+clientSocket.getPort());
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Got something");
                        out.println(inputLine);
                    }
                }finally{
                    System.out.println("Ended connection");
                }
            }
        }
    }
}
