package photos.brooklyn.lang;

import photos.brooklyn.sockets.BruteForceCoding;

import java.nio.ByteBuffer;

public class ByteManipulation {
    private ByteManipulation(){}

    public static void main(String[] args) {
        final byte c = (byte) "a".toCharArray()[0];
        System.out.println(c);

        final long l = 123456787654321l;
        final byte[] bytes = getBytes(l);
        System.out.println(bytes.length);
        System.out.println(BruteForceCoding.byteArrayToDecimalString(bytes));

        System.out.println(Integer.toBinaryString(20));
        System.out.println(Integer.toBinaryString(-20));

        final byte neg = -12;
        System.out.printf("%d%n", Byte.toUnsignedInt(neg));
        System.out.println(Integer.toBinaryString(244));
        System.out.println(Integer.toBinaryString(12));
    }

    private static byte[] getBytes(final long l) {
        final ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
        bb.putLong(l);
        return bb.array();
    }
}
