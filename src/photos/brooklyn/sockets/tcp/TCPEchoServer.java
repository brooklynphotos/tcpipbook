package photos.brooklyn.sockets.tcp;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPEchoServer {
    public static void main(String[] args) throws IOException {
        final int serverPort = Integer.parseInt(args[0]);
        try(final ServerSocket socket = new ServerSocket(serverPort)) {
            int receivedSize;
            final byte[] buffer = new byte[1024];
            final Console console = System.console();
            while (true) {
                try(final Socket clientSocket = socket.accept()) {
                    final SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
                    console.printf("Connecting to %s\n",clientAddress);
                    try (
                        final InputStream is = clientSocket.getInputStream();
                        final OutputStream os = clientSocket.getOutputStream()
                    ) {
                        while ((receivedSize = is.read(buffer)) != -1) {
                            console.printf("writing a block size=%d\n",receivedSize);
                            os.write(buffer, 0, receivedSize);
                            if(console.readLine().equals("disconnect")){
                                console.printf("Server disconnecting...\n");
                                break;
                            }
                            console.printf("Ended a read\n");
                        }
                        console.printf("disconnected from %s",clientAddress);

                    }
                    console.printf("Checking to see if we are quitting\n");
                    if(console.readLine().equals("quit")){
                        break;
                    }
                }
            }
        }
    }
}
