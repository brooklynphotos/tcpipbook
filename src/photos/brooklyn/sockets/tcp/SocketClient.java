package photos.brooklyn.sockets.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClient {
    public SocketClient(String[] params) throws IOException {
        final InetAddress serverAddress = InetAddress.getByName(params[0]);
        final int port = Integer.parseInt(params[1]);
        final String msg = params[2];
        try(
            final Socket socket = new Socket(serverAddress, port);
            final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))
        ){
            out.println(msg);
            String line;
            while((line=in.readLine()) != null){
                System.out.println(">>>"+line);
                if(line.startsWith("bye")) break;
                final String fromReader = stdin.readLine();
                if(fromReader != null){
                    out.println(fromReader);
                }
            }
        }

    }
}
