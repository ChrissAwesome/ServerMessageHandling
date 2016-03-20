package Connection;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by christian on 20.03.2016.
 */
public class ServerSocket extends java.net.ServerSocket
{
    public ServerSocket(int port) throws IOException
    {
        super(port);
    }
}
