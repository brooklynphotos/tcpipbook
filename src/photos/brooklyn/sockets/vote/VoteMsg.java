package photos.brooklyn.sockets.vote;

public class VoteMsg {
    private boolean isInquiry;
    private boolean isResponse;
    private int candidateId;

    private long voteCount;

    public static final int MAX_CANDIDATE_ID = 1000;


    public VoteMsg(final boolean isInquiry, final boolean isResponse, final int candidateId, final long voteCount) {
        if(voteCount != 0 && !isResponse)
            throw new IllegalArgumentException("Request vote count must be zero when it's not a response");
        if (candidateId < 0 || candidateId > MAX_CANDIDATE_ID) {
            throw new IllegalArgumentException("Bad candidate id: " + candidateId);
        }
        if (voteCount < 0) {
            throw new IllegalArgumentException("No negative vote counts: " + voteCount);
        }
        this.isInquiry = isInquiry;
        this.isResponse = isResponse;
        this.candidateId = candidateId;
        this.voteCount = voteCount;
    }

    public boolean isInquiry() {
        return isInquiry;
    }

    public boolean isResponse() {
        return isResponse;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public long getVoteCount() {
        return voteCount;
    }

    @Override
    public String toString() {
        String res = (isInquiry ? "inquiry" : "vote") + " for candidate " + candidateId;
        if (isResponse) {
            res = "response to " + res + " who now has " + voteCount + " vote(s)";
        }
        return res;
    }
}
