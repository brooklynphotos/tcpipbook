package photos.brooklyn.sockets.vote;

import photos.brooklyn.sockets.Framer;
import photos.brooklyn.sockets.LengthFramer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class VoteClientTCP {
    public static final int CANDIDATE_ID = 888;

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Parameters(s): <Server> <Port>");
        }

        final String destAddr = args[0];
        final int destPort = Integer.parseInt(args[1]);

        try(
                final Socket sock = new Socket(destAddr, destPort);
                final OutputStream out = sock.getOutputStream()
        ){
            VoteMsgCoder coder = new VoteMsgBinCoder();
            // change length to delim for a different encoding strategy
            Framer framer = new LengthFramer(sock.getInputStream());

            // inquiry request
            final VoteMsg inqRequestMsg = new VoteMsg(true, false, CANDIDATE_ID, 0);
            byte[] encodedMsg = coder.toWire(inqRequestMsg);

            // send msg
            System.out.printf("Sending inquiry (%d bytes)", encodedMsg.length);
            System.out.println(inqRequestMsg);
            // sends the encoded msg in bytes to the output stream
            framer.frameMsg(encodedMsg, out);

            // vote request
            final VoteMsg voteRequestMsg = new VoteMsg(false, false, CANDIDATE_ID, 0);
            encodedMsg = coder.toWire(voteRequestMsg);
            System.out.printf("Sending Vote (%d bytes)",encodedMsg.length);
            framer.frameMsg(encodedMsg, out);

            // receive inquiry
            encodedMsg = framer.nextMsg();
            System.out.printf("Received Inquiry Response (%d bytes)", encodedMsg.length);
            final VoteMsg inqResponseMsg = coder.fromWire(encodedMsg);
            System.out.println(inqResponseMsg);

            // receive vote response
            encodedMsg = framer.nextMsg();
            System.out.printf("Received Vote Response (%d bytes)", encodedMsg.length);
            final VoteMsg voteResponseMsg = coder.fromWire(encodedMsg);
            System.out.println(voteResponseMsg);
        }
    }
}
