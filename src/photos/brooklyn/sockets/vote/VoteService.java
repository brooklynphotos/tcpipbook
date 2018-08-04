package photos.brooklyn.sockets.vote;

import java.util.HashMap;
import java.util.Map;

public class VoteService {
    // in-memory db of candidate to votes
    private Map<Integer, Long> results = new HashMap<>();

    public VoteMsg handleRequest(VoteMsg msg) {
        // if it's a response already, sned it back
        if (msg.isResponse()) {
            return msg;
        }
        msg.setResponse(true);
        // get candidate id and vote count
        final int candidate = msg.getCandidateId();
        Long count = results.get(candidate);
        if (count == null) {
            count = 0l;
        }
        // if this a casting of vote, increment count
        if (!msg.isInquiry()) {
            results.put(candidate, ++count);
        }
        msg.setVoteCount(count);
        return msg;
    }
}
