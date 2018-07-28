package photos.brooklyn.sockets.vote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

public class VoteMsgTextCoder implements VoteMsgCoder {
    /*
     * Wire format "VOTEPROTO" <"v"|"i"> [<RESPFLAG] <CANDIDATE> [<VOTECNT>]
     * Charset is fixed by the wire format
     */
    // Manifest constants for encoding
    public static final String MAGIC = "Voting";
    public static final String VOTESTR = "v";
    public static final String INQSTR = "i";
    public static final String RESPONSESTR = "R";

    public static final String CHARSETNAME = "US-ASCII";
    public static final String DELIMITER = " ";
    public static final int MAX_WIRE_LENGTH = 2000;

    @Override
    public byte[] toWire(final VoteMsg msg) {
        final String msgString = MAGIC + DELIMITER
                + (msg.isInquiry() ? INQSTR : VOTESTR) + DELIMITER
                + (msg.isResponse() ? RESPONSESTR + DELIMITER : "")
                + Integer.toString(msg.getCandidateId()) + DELIMITER
                + Long.toString(msg.getVoteCount());
        return msgString.getBytes(Charset.forName(CHARSETNAME));
    }

    @Override
    public VoteMsg fromWire(final byte[] message) throws IOException {
        String token;
        boolean isInquiry;
        boolean isResponse = false;
        int candidateId = 0;
        long voteCount = 0l;
        try (
            final ByteArrayInputStream msgStream = new ByteArrayInputStream(message);
            final Scanner s = new Scanner(new InputStreamReader(msgStream, CHARSETNAME))
        ) {
            token = s.next();
            if (!token.equals(MAGIC)) {
                throw new IOException("Bad magic string: " + token);
            }
            token = s.next();
            if(token.equals(VOTESTR)){
                isInquiry = false;
            } else if (token.equals(INQSTR)) {
                isInquiry = true;
            }else{
                throw new IOException("Bad vote/inq indicator: " + token);
            }
            token = s.next();
            if (token.equals(RESPONSESTR)) {
                isResponse = true;
                token = s.next();
            }
            // candidate
            candidateId = Integer.parseInt(token);
            if(isResponse){
                voteCount = s.nextLong();
            }
        }
        return new VoteMsg(isInquiry, isResponse, candidateId, voteCount);
    }
}
