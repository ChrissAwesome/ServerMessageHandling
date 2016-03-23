package MessageData;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import Connection.Connection;


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


    //Main memory for messages in the ram
    public static ConcurrentHashMap<String, List<MessageCont>> messagePool = new ConcurrentHashMap<>();

    private void checkForOldMessages() throws IOException {
        while (true)
        {
            for(List<MessageCont> messages : messagePool.values())
            {
                for (MessageCont message: messages)
                {
                    if(message.messageDate.before(lastTimeWrittenOnPlate) && message.dataIsOnPlate == false)
                    {
                        FileOutputStream out = new FileOutputStream(dataLocationPath + "/" + message.to + df.format(message.messageDate) + ".message", false);
                        out.write(message.data);
                        out.close();

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
                System.out.println("Error while trying to clean up.:");
                System.out.println(e.getMessage());
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
    public void saveMessagePoolOnDisk()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(dataLocationPath + "/MessagePool/messagePoolObject.obj");
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

    @Override
    public void run()
    {
        try
        {

            checkForOldMessages();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
