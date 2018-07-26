package photos.brooklyn.sockets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DelimFramer implements Framer {
    private InputStream in;
    private static final byte DELIMITER = '\n';

    public DelimFramer(InputStream in){
        this.in = in;
    }

    @Override
    public void frameMsg(final byte[] message, final OutputStream out) throws IOException {
        for(final byte b : message){
            if(b==DELIMITER) throw new IllegalArgumentException("Contains the delimiter!");
        }

        out.write(message);
        out.write(DELIMITER);
        out.flush();
    }

    @Override
    public byte[] nextMsg() throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nextByte;
        while((nextByte=in.read()) != DELIMITER){
            if(nextByte==-1) {
                if (buffer.size() == 0) {
                    return null;
                } else {
                    throw new IllegalArgumentException("Strange, got the end of the message without encountering the delimiter");
                }
            }
            buffer.write(nextByte);
        }
        return buffer.toByteArray();
    }
}
