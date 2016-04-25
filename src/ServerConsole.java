import MessageData.DataHandling;

import java.util.Scanner;

/**
 * Created by christian on 06.04.2016.
 */
public class ServerConsole implements Runnable
{

    @Override
    public void run()
    {
        Scanner input = new Scanner(System.in);

        while (true)
        {
            input.hasNextLine();
            String command = input.nextLine();

            switch (command)
            {
                case "help":
                    helpCommand();
                    break;
                case "stop":
                    stopCommand();
                    break;
                case "shutdown":
                    shutdownCommand();
                    break;
                case "load messages":
                    loadMessagesCommand();
                    break;
                default:
                    System.out.println("no command found");
            }
        }
    }

    //All Command functions
    private static void helpCommand()
    {
        System.out.println("all commands:");
        System.out.println("stop //safe shutdown");
        System.out.println("shutdown //shutdown without saving");
        System.out.println("load messages //load messages from disk");
    }

    private static void stopCommand()
    {
        System.out.println("stopping the server...");
        DataHandling.saveMessagePoolOnDisk();
        System.exit(0);
    }

    private static void shutdownCommand()
    {
        System.exit(0);
    }

    private static void loadMessagesCommand()
    {
        System.out.println("load messages...");
        DataHandling.loadMessagePoolFromDisk();
        System.out.println("loading messages finished");
    }
}
