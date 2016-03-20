package Connection;

import java.io.IOException;
import java.net.*;
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
            Scanner wasd = new Scanner(m_socket.getInputStream());
            System.out.println(wasd.nextLine());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
