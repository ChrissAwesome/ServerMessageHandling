package Connection;

import MessageData.DataHandling;
import MessageData.MessageCont;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by christian on 20.03.2016.
 */
public class Connection  implements Runnable
{
    private Socket m_socket;

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
            InputStream in = m_socket.getInputStream();
            ObjectInputStream oStream = new ObjectInputStream(in);
            MessageCont messageContFromClient = (MessageCont) oStream.readObject();

            //If m_to is null, look for messages for m_from
            if(messageContFromClient.to == null)
            {
                List<MessageCont> messages = DataHandling.messagePool.get(messageContFromClient.from);
                if(messages != null && messages.size() != 0)
                {
                    for (MessageCont message : messages)
                    {
                        //send all messages to the client
                        if(message.dataIsOnPlate == true)
                        {
                            //load the data from the hard disk
                            message.data = DataHandling.getDataFromHardDisk(messageContFromClient.from, message.messageDate);
                        }

                        //Send the message
                        OutputStream out = m_socket.getOutputStream();
                        ObjectOutputStream objectStreamToClient = new ObjectOutputStream(out);
                        objectStreamToClient.writeObject(message);
                        objectStreamToClient.close();
                    }
                }
                else
                {
                    //Send empty message
                    MessageCont message = new MessageCont("", messageContFromClient.from, true, true, true, true, "No Messages");
                    OutputStream out = m_socket.getOutputStream();
                    ObjectOutputStream objectStreamToClient = new ObjectOutputStream(out);
                    objectStreamToClient.writeObject(message);
                    objectStreamToClient.close();
                }
                DataHandling.messagePool.remove(messageContFromClient.from);
            }
            //If the rest is filled, then save the message in the messagePool
            else if(!messageContFromClient.from.isEmpty() && !messageContFromClient.to.isEmpty())
            {
                if(DataHandling.messagePool.get(messageContFromClient.to) != null)
                {
                    //Nachrichten schon vorhanden
                    DataHandling.messagePool.get(messageContFromClient.to).add(messageContFromClient);
                }
                else
                {
                    //Erste Nachricht
                    List<MessageCont> content = new ArrayList<>();
                    content.add(messageContFromClient);
                    DataHandling.messagePool.put(messageContFromClient.to, content);
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public byte[] concat(byte[] a, byte[] b)
    {
        for (int i = 0; i < a.length; i++)
        {
            if(b.length > i)
            {
                a[i] = b[i];
            }
            else
            {
                a[i] = 0;
            }
        }
        return a;
    }
}
