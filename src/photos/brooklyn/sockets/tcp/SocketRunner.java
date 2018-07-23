package photos.brooklyn.sockets.tcp;

import java.io.IOException;
import java.util.Arrays;

public class SocketRunner {

    public static void main(String[] args) throws IOException {
        final String[] params = Arrays.copyOfRange(args, 1, args.length);
        if(args[0].equals("-h")){
            new SocketServer(params);
        }else{
            new SocketClient(params);
        }
    }
}
