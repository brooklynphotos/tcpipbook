package photos.brooklyn.sockets.vote;

import photos.brooklyn.sockets.Framer;
import photos.brooklyn.sockets.LengthFramer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VoteServerTCP {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Parameter(s): <port>");
        }
        int port = Integer.parseInt(args[0]);
        try(final ServerSocket servSock = new ServerSocket(port)){
            final VoteMsgCoder coder = new VoteMsgBinCoder();
            final VoteService svc = new VoteService();
            while (true) {
                try (
                    final Socket clntSock = servSock.accept();
                    final InputStream in = clntSock.getInputStream();
                    final OutputStream out = clntSock.getOutputStream()
                ) {
                    System.out.println("Handling client...");
                    final Framer framer = new LengthFramer(in);
                    byte[] req;
                    while ((req = framer.nextMsg()) != null) {
                        System.out.printf("Received message (%s) bytes\n", req.length);
                        final VoteMsg responseMsg = svc.handleRequest(coder.fromWire(req));
                        framer.frameMsg(coder.toWire(responseMsg), out);
                    }
                }
            }
        }

    }
}
