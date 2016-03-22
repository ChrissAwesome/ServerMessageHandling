package Connection;

import MessageData.DataHandling;
import MessageData.MessageCont;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christian on 20.03.2016.
 */
public class Connection  implements Runnable
{
    private Socket m_socket;

    private String m_from = null;
    private String m_to = null;
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
            for (int i = 0; i < 31; i++)
            {
                if(readBuffer[i] != 0)
                {
                    m_from += (char)readBuffer[i];
                }
            }

            //read to
            for (int i = 32; i < 64; i++)
            {
                if(readBuffer[i] != 0)
                {
                    m_to += (char)readBuffer[i];
                }
            }

            //read isUTF8Text
            if(readBuffer[128] == 1)
            {
                m_isUTF8Text = true;
            }

            //read isSoundFile
            if(readBuffer[129] == 1)
            {
                m_isSoundFile = true;
            }

            //read isVideoFile
            if(readBuffer[130] == 1)
            {
                m_isVideoFile = true;
            }

            //read flag
            if(readBuffer[131] == 1)
            {
                m_isPictureFile = true;
            }

            //Data to send
            int dataStart = 132;
            byte[] dataBuffer = new byte[readBuffer.length-dataStart];
            for (int i = dataStart; i < readBuffer.length; i++)
            {
                dataBuffer[i-dataStart] = readBuffer[i];
            }

            //If m_to is null, look for messages for m_from
            if(m_to == null)
            {
                List<MessageCont> messages = DataHandling.messagePool.get(m_from);
                if(messages != null && messages.size() != 0)
                {
                    for (int i = 0; i < messages.size(); i++)
                    {
                        //messages[i]; übertrage alle nachrichten
                    }
                }
            }
            //If the rest is filled, then save the message in the messagePool
            else if(!m_from.isEmpty() && !m_to.isEmpty())
            {
                MessageCont messageCont = new MessageCont(m_from,m_isUTF8Text, m_isSoundFile, m_isVideoFile, m_isPictureFile, dataBuffer);
                if(DataHandling.messagePool.get(m_to) != null)
                {
                    //Nachrichten schon vorhanden
                    DataHandling.messagePool.get(m_to).add(messageCont);
                }
                else
                {
                    //Erste Nachricht
                    List<MessageCont> content = new ArrayList<>();
                    content.add(messageCont);
                    DataHandling.messagePool.put(m_to, content);
                }
            }
            else
            {

            }
        }
        catch (IOException e)
        {
            System.out.println("Error in the Message-Handling");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
