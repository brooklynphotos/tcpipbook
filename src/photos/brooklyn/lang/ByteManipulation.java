package photos.brooklyn.lang;

import photos.brooklyn.sockets.ByteUtils;

import java.nio.ByteBuffer;

public class ByteManipulation {
    private ByteManipulation(){}

    public static void main(String[] args) {
//        twosComplement();
        shifts();
    }

    private static final void shifts() {
        int a = 2;
        int aLeft3 = a << 3;
        int b = 123456;
        int bRight3 = b >> 3;
        System.out.printf("a=%s%n",Integer.toBinaryString(a));
        System.out.printf("a<<3=%s%n",Integer.toBinaryString(aLeft3));
        System.out.printf("b=\t\t%s%n",Integer.toBinaryString(b));
        System.out.printf("b>>3=\t%s%n",Integer.toBinaryString(bRight3));

    }
    private static final void twosComplement() {
        final byte a = 20;
        final byte notA = ~a;
        final byte notAPlus1 = notA + 1;
        System.out.printf("a=%s%n",Integer.toBinaryString(a));
        System.out.printf("~a=%s%n",Integer.toBinaryString(notA));
        System.out.printf("~a+1=%s%n",Integer.toBinaryString(notAPlus1));
        System.out.printf("final=%d", notAPlus1);
    }

    private static final void exercise1() {
        final byte c = (byte) "a".toCharArray()[0];
        System.out.println(c);

        final long l = 123456787654321l;
        final byte[] bytes = getBytes(l);
        System.out.println(bytes.length);
        System.out.println(ByteUtils.byteArrayToDecimalString(bytes));

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
