package photos.brooklyn.sockets;

public class BruteForceCoding {
    private static final byte byteVal = 101;
    private static final short shortVal = 10_001;
    private static final int intVal = 100_000_001;
    private static final long longVal = 1_000_000_000_001l;

    private static final int BSIZE = Byte.SIZE/Byte.SIZE;
    private static final int SSIZE = Short.SIZE/Byte.SIZE;
    private static final int ISIZE = Integer.SIZE/Byte.SIZE;
    private static final int LSIZE = Long.SIZE/Byte.SIZE;

    private static final int BYTEMASK = 0xFF; // 8 bit

    public static final String byteArrayToDecimalString(byte[] bArray){
        final StringBuilder rtn = new StringBuilder();
        for (byte b : bArray) {
            rtn.append(b & BYTEMASK).append(" ");
        }
        return rtn.toString();
    }

    public static final int encodeIntBigEndian(byte[] dst, long val, int offset, int size) {
        for (int i = 0; i < size; i++) {
            dst[offset++] = (byte) (val >> ((size - i - 1) * Byte.SIZE));
        }
        return offset;
    }

    public static final long decodeIntBigEndian(byte[] val, int offset, int size) {
        long rtn = 0;
        for (int i = 0; i < size; i++) {
            rtn = (rtn << Byte.SIZE) | ((long) val[offset + i] & BYTEMASK);
        }
        return rtn;
    }

    public static void main(String[] args) {
        final byte[] message = new byte[BSIZE + SSIZE + ISIZE + LSIZE];
        // encode the fields in the target byte array
        int offset = 0;
        offset = encodeIntBigEndian(message, byteVal, offset, BSIZE);
        offset = encodeIntBigEndian(message, shortVal, offset, SSIZE);
        offset = encodeIntBigEndian(message, intVal, offset, ISIZE);
        offset = encodeIntBigEndian(message, longVal, offset, LSIZE);
        System.out.printf("Ended encoding (offset=%d): %s%n", offset, byteArrayToDecimalString(message));

        // decode several fields
        long value = decodeIntBigEndian(message, BSIZE, SSIZE);
        System.out.printf("Decoded short = %,d%n", value);
        value = decodeIntBigEndian(message, BSIZE + SSIZE + ISIZE, LSIZE);
        System.out.printf("Decoded long = %,d%n", value);

        // possible problems with simple casting
        offset = 4;
        value = decodeIntBigEndian(message, offset, BSIZE);
        System.out.printf("Decoded value (offset=%d, size=%d) = %,d%n",offset, BSIZE, value);
        byte bVal = (byte) decodeIntBigEndian(message, offset, BSIZE);
        System.out.printf("Same value in bytes: %d%n", bVal);
    }
}
