package photos.brooklyn.sockets;

import java.io.IOException;
import java.io.OutputStream;

public interface Framer {
    /**
     * adds framing information and outputs a given message to a given stream
     * @param message
     * @param out
     * @throws IOException
     */
    void frameMsg(byte[] message, OutputStream out) throws IOException;

    /**
     * scans a given stream, extracting the next message
     * @return
     * @throws IOException
     */
    byte[] nextMsg() throws IOException;
}
