package MessageData;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import Connection.Connection;

import static java.lang.System.load;
import static java.lang.System.out;


/**
 * Created by christian on 22.03.2016.
 */
public class DataHandling implements Runnable
{
    public static String dataLocationPath = "C:\\Users\\christian\\Desktop\\Testdaten";
    public static long cleanUpIntervall = 1200000;
    private Date lastTimeWrittenOnPlate = new Date();
    private char seperator;
    private static DateFormat df = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
    private static String messagePoolFilePath = dataLocationPath + "/MessagePool/messagePoolObject.obj";

    //Main memory for messages in the ram
    public static ConcurrentHashMap<String, List<MessageCont>> messagePool = new ConcurrentHashMap<>();


    @Override
    public void run()
    {
        try
        {
            if(loadMessagePoolFromDisk() != null)
            {
                messagePool = loadMessagePoolFromDisk();
            }

            checkForOldMessages();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void checkForOldMessages() throws IOException {
        while (true)
        {
            for(List<MessageCont> messages : messagePool.values())
            {
                for (MessageCont message: messages)
                {
                    if(message.messageDate.before(lastTimeWrittenOnPlate) && message.dataIsOnPlate == false)
                    {
                        String fileToLookFor = dataLocationPath + "/" + message.to + df.format(message.messageDate) + ".message";
                        if(new File(fileToLookFor).isFile())
                        {
                            FileOutputStream out = new FileOutputStream(fileToLookFor, false);
                            out.write(message.data);
                            out.close();
                        }
                        message.data = null;
                        message.dataIsOnPlate = true;
                        message.dataPlatePath = dataLocationPath;
                    }
                }
            }

            lastTimeWrittenOnPlate = new Date();
            try
            {
                Thread.sleep(cleanUpIntervall);
            }
            catch (InterruptedException e)
            {
                out.println("Error while trying to clean up.:");
                out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static byte[] getDataFromHardDisk(String owner, Date messageDate) throws IOException
    {
        String pathToData = dataLocationPath + "/" + owner + df.format(messageDate) + ".message";
        List<MessageCont> messageToReturn = new ArrayList<>();
        FileInputStream reader = new FileInputStream(pathToData);
        int curOffSet = 0;
        byte[] data = new byte[Connection.dataStart];
        reader.read(data);

        //delete the file, because its not need anymore
        File file = new File(pathToData);
        file.delete();

        return data;
    }

    //Safe the messagePool as backup and for server shutdowns
    public static void saveMessagePoolOnDisk()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(messagePoolFilePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(messagePool);
            out.close();
            fileOut.close();
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    public static ConcurrentHashMap<String, List<MessageCont>> loadMessagePoolFromDisk()
    {
        ConcurrentHashMap<String, List<MessageCont>> messagePoolToReturn = null;
        try
        {
            if(new File(messagePoolFilePath).isFile())
            {
                FileInputStream fileIn = new FileInputStream(messagePoolFilePath);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                messagePoolToReturn = (ConcurrentHashMap<String, List<MessageCont>>)in.readObject();
                in.close();
                fileIn.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return messagePoolToReturn;
    }
}
