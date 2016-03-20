package Connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by christian on 20.03.2016.
 */
public class Connection  implements Runnable
{
    private Socket m_socket;
    private int connectionID;

    public Connection(Socket socket, int connectionID)
    {
        m_socket = socket;
        try
        {
            m_socket.setSoTimeout(300);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
    }

    public int getConnectionID()
    {
        return connectionID;
    }

    @Override
    public void run()
    {
        try
        {
            byte[] readBuffer = new byte[1024];
            InputStream in = m_socket.getInputStream();
            in.read(readBuffer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
