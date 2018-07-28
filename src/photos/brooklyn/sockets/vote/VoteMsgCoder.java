package photos.brooklyn.sockets.vote;

import java.io.IOException;

/**
 * serializer and deserializer for the vote message {@link VoteMsg}
 */
public interface VoteMsgCoder {
    byte[] toWire(VoteMsg msg) throws IOException;
    VoteMsg fromWire(byte[] input) throws IOException;
}
