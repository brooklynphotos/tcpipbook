package photos.brooklyn.sockets;

public class ByteUtils {
    private static final int BYTEMASK = 0xFF; // 8 bit

    public static final String byteArrayToDecimalString(byte[] bArray){
        final StringBuilder rtn = new StringBuilder();
        for (byte b : bArray) {
            // b & BYTEMASK converts it to an unsigned int
            rtn.append(b & BYTEMASK).append(" ");
        }
        return rtn.toString();
    }
}
