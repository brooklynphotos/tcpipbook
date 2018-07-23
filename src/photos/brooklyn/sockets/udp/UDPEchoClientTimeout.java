package photos.brooklyn.sockets.udp;

import java.io.Console;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPEchoClientTimeout {
    private static final int TIMEOUT = 3000;
    private static final int MAXTRIES = 5;

    public static void main(String[] args) throws IOException {
        final InetAddress serverAddress = InetAddress.getByName(args[0]);
        final byte[] msg = args[1].getBytes();
        final int serverPort = Integer.parseInt(args[2]);

        boolean receivedResponse = false;
        final Console console = System.console();
        try(final DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT);
            DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, serverAddress, serverPort);

            // the server info will be filled by the receive call
            DatagramPacket receivePacket = new DatagramPacket(new byte[msg.length], msg.length);
            int tries = 0;
            do {
                socket.send(sendPacket);
                try{
                    // wait for a response from the server
                    socket.receive(receivePacket);
                    // make sure we are getting a response frm the server and not somewhere else
                    if(!receivePacket.getAddress().equals(serverAddress)){
                        throw new IOException("Received a packet from someone else: "+receivePacket.getAddress());
                    }
                    receivedResponse = true;
                }catch(InterruptedIOException ix){
                    // didn't get anything as UDP doesn't guarantee anything
                    tries++;
                    console.printf("Time out, doing another %d tries\n", MAXTRIES-tries);
                }
            }while(!receivedResponse && tries<MAXTRIES);
            if(receivedResponse){
                System.console().printf("Received %s\n", new String(receivePacket.getData()));
            }else{
                System.console().printf("Ran out of tries\n");
            }
        }
    }
}
