package photos.brooklyn.sockets.vote;

import java.io.*;

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
        // flipping 1 and 0 on the bits
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
        // sanity check
        if (input.length < MIN_WIRE_LENGTH) {
            throw new IOException("Run message");
        }
        try (
                final ByteArrayInputStream bs = new ByteArrayInputStream(input);
                final DataInputStream in = new DataInputStream(bs)
        ) {
            final int magic = in.readShort();
            if ((magic & MAGIC_MASK) != MAGIC) {
                throw new IOException("Bad magic #:" + ((magic & MAGIC_MASK) >> MAGIC_SHIFT));
            }
            // reading single bits for boolean
            final boolean resp = ((magic & RESPONSE_FLAG) != 0);
            final boolean inq = ((magic & INQUIRE_FLAG) != 0);
            final int candidateId = in.readShort();
            if (candidateId < 0 || candidateId > 1000) {
                throw new IOException("Bad candidate id: " + candidateId);
            }
            long count = 0;
            if (resp) {
                count = in.readLong();
                if (count < 0) {
                    throw new IOException("Bad vote count: " + count);
                }
            }
            // Ignore any extra bytes
            return new VoteMsg(inq, resp, candidateId, count);
        }

    }
}
