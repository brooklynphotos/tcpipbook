package photos.brooklyn.sockets.vote;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The message comprises two shorts and a long;
 * first short comprises 6 bits of magic number, two bits each for the flags, and then zeros;
 * second short is the candidate id;
 * then the long is the vote count
 */
public class VoteMsgBinCoder implements VoteMsgCoder {
    public static final int MIN_WIRE_LENGTH = 4;
    public static final int MAX_WIRE_LENGTH = 16;
    public static final int MAGIC = 0x5400;
    public static final int MAGIC_MASK = 0xfc00;
    public static final int MAGIC_SHIFT = 8;
    public static final int RESPONSE_FLAG = 0x0200;
    public static final int INQUIRE_FLAG = 0x0100;

    @Override
    public byte[] toWire(final VoteMsg msg) throws IOException {
        short magicAndFlags = MAGIC;
        if (msg.isInquiry()) {
            magicAndFlags |= INQUIRE_FLAG;
        }
        if (msg.isResponse()) {
            magicAndFlags |= RESPONSE_FLAG;
        }
        try(
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(byteStream)
        ){
            out.writeShort(magicAndFlags);
            // candidate id can be short since it's less than 1000
            out.writeShort((short) msg.getCandidateId());
            if (msg.isResponse()) {
                out.writeLong(msg.getVoteCount());
            }
            out.flush();
            final byte[] data = byteStream.toByteArray();
            return data;
        }
    }

    @Override
    public VoteMsg fromWire(final byte[] input) throws IOException {
        return null;
    }
}
