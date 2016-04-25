import Connection.Connection;
import Connection.ServerSocket;
import MessageData.DataHandling;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by christian on 20.03.2016.
 */
public class Main
{
    public static void main(String[] args)
    {
        int portNumber;
        if(args.length == 0)
        {
            portNumber = 7777;
            System.out.println("Kein Port angegeben!");
            System.out.println("Verwende Standartport " + portNumber);
        }
        else
        {
            portNumber = Integer.parseInt(args[0]);
        }

        ServerSocket server = null;
        try
        {
            server = new ServerSocket(portNumber);
        }
        catch (IOException e)
        {
            System.out.println("Error while creating the server:");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        //Start thread for the message handling
        Thread dataHandleThread = new Thread(new DataHandling());
        dataHandleThread.start();

        //Start thread for the server console
        Thread serverConsoleThread = new Thread(new ServerConsole());
        serverConsoleThread.start();

        //Accept multiple clients
        while (true)
        {
            try
            {
                Socket clientSocket = server.accept();
                Thread th = new Thread(new Connection(clientSocket));
                th.start();
            }
            catch (IOException e)
            {
                System.out.println("Error while listening:");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
