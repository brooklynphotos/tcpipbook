package photos.brooklyn.sockets.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPEchoServer {
    private static final int ECHOMAX = 255;

    public static void main(String[] args) throws IOException {
        final int serverPort = Integer.parseInt(args[0]);
        try(final DatagramSocket socket = new DatagramSocket(serverPort)) {
            final DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
            while (true) {
                System.console().printf("Waiting for a connection...\n");
                socket.receive(packet); // block to wait for packet
                System.console().printf("Received packet of length %d from client at %s:%d\n", packet.getLength(), packet.getAddress().getHostAddress(), packet.getPort());
                socket.send(packet);
                packet.setLength(ECHOMAX);
                if("d".equals(System.console().readLine())) break;
            }
            System.console().printf("Done\n");
        }
    }
}
