package photos.brooklyn.sockets.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPEchoClient {
    /**
     * 1. server
     * 2. what to say
     * 3. port (optional)
     * @param args
     */
    public static void main(String[] args) throws IOException {
        final String server = args[0];
        final byte[] message = args[1].getBytes();
        final int port = args.length==3 ? Integer.parseInt(args[2]) : 7;
        try(
            final Socket socket = new Socket(server, port);
            final InputStream socketInput = socket.getInputStream();
            final OutputStream socketOutput = socket.getOutputStream()
        ){
            socketOutput.write(message);
            socketOutput.flush();
            // read from the input
            final byte[] bytes = new byte[1024];
            int resp;
            while((resp = socketInput.read(bytes))!= -1){
                System.out.write(bytes,0,resp);
            }
        }
    }
}
