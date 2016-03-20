package Connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * Created by christian on 20.03.2016.
 */
public class Connection  implements Runnable
{
    private Socket m_socket;
    private long m_connectionID;

    private String m_from;
    private String m_to;
    private boolean m_isUTF8Text = false;
    private boolean m_isSoundFile = false;
    private boolean m_isVideoFile = false;
    private boolean m_IsPictureFile = false;

    public Connection(Socket socket, long connectionID)
    {
        m_socket = socket;
        m_connectionID = connectionID;
        try
        {
            m_socket.setSoTimeout(300);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
    }

    public long getConnectionID()
    {
        return m_connectionID;
    }

    public void setConnectionID(long id)
    {
        m_connectionID = id;
    }

    @Override
    public void run()
    {
        try
        {
            byte[] readBuffer = new byte[1024];
            InputStream in = m_socket.getInputStream();
            in.read(readBuffer);

            //read from
            for (int i = 0; i < 64; i++)
            {
                if(readBuffer[i] != 0)
                {
                    m_from += (char)readBuffer[i];
                }
            }

            //read to
            for (int i = 64; i < 129; i++)
            {
                m_to += (char)readBuffer[i];
            }

            //read flag
            if(readBuffer[129] == 1)
            {
                m_isUTF8Text = true;
            }

            //read flag
            if(readBuffer[130] == 1)
            {
                m_isSoundFile = true;
            }

            //read flag
            if(readBuffer[131] == 1)
            {
                m_isVideoFile = true;
            }

            //read flag
            if(readBuffer[132] == 1)
            {
                m_IsPictureFile = true;
            }

            //Data to send
            for (int i = 133; i < readBuffer.length; i++)
            {

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
