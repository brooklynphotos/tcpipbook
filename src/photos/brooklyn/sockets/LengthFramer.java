package photos.brooklyn.sockets;

import java.io.*;

public class LengthFramer implements Framer {
    public static final int MAX_MESSAGE_LENGTH=(1 << 16) - 1;
    public static final int BYTE_MASK = 0xff;
    public static final int SHORT_MASK = 0xffff;
    public static final int BYTE_SHIFT = 8;

    private final DataInputStream in;

    public LengthFramer(final InputStream in){
        this.in = new DataInputStream(in);
    }

    @Override
    public void frameMsg(final byte[] message, final OutputStream out) throws IOException {
        if(message.length>MAX_MESSAGE_LENGTH) throw new IllegalArgumentException("Too long");
        out.write((message.length >> BYTE_SHIFT) & BYTE_MASK);
        out.write(message);
        out.flush();
    }

    @Override
    public byte[] nextMsg() throws IOException {
        int length;
        try {
            length = in.readUnsignedShort();
        } catch (EOFException e) {
            return null;
        }
        byte[] msg = new byte[length];
        in.readFully(msg);
        return msg;
    }
}
