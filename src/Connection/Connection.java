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

    private String m_from;
    private String m_to;
    private boolean m_isUTF8Text = false;
    private boolean m_isSoundFile = false;
    private boolean m_isVideoFile = false;
    private boolean m_isPictureFile = false;

    public Connection(Socket socket)
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

    @Override
    public void run()
    {
        try
        {
            byte[] readBuffer = new byte[1024];
            InputStream in = m_socket.getInputStream();
            in.read(readBuffer);

            //read from
            char[] fromBuffer = new char[31];
            for (int i = 0; i < 64; i++)
            {
                if(readBuffer[i] != 0)
                {
                    fromBuffer[i] = (char)readBuffer[i];
                }
            }
            m_from = String.copyValueOf(fromBuffer);

            //read to
            char[] toBuffer = new char[31];
            for (int i = 64; i < 129; i++)
            {
                if(readBuffer[i] != 0)
                {
                    toBuffer[i-64] = (char)readBuffer[i];
                }
            }
            m_to = String.valueOf(toBuffer);

            //read isUTF8Text
            if(readBuffer[129] == 1)
            {
                m_isUTF8Text = true;
            }

            //read isSoundFile
            if(readBuffer[130] == 1)
            {
                m_isSoundFile = true;
            }

            //read isVideoFile
            if(readBuffer[131] == 1)
            {
                m_isVideoFile = true;
            }

            //read flag
            if(readBuffer[132] == 1)
            {
                m_isPictureFile = true;
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
