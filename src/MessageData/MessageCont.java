package MessageData;

import java.util.Date;

/**
 * Created by christian on 22.03.2016.
 */
public class MessageCont
{
    public String from;
    public String to; //Message Owner

    public boolean isUTF8Text = false;
    public boolean isSoundFile = false;
    public boolean isVideoFile = false;
    public boolean isPictureFile = false;

    public byte[] data;

    public Date messageDate = new Date();


    public boolean dataIsOnPlate = false;
    public String dataPlatePath = "";

    public MessageCont(String messageFrom, String messageTo, boolean isUTF8Text , boolean isSoundFile , boolean isVideoFile, boolean isPictureFile, byte[] data)
    {
        this.from = messageFrom;
        this.to = messageTo;

        this.isUTF8Text = isUTF8Text;
        this.isSoundFile = isSoundFile;
        this.isVideoFile = isVideoFile;
        this.isPictureFile = isPictureFile;

       this.data = data;
    }
}
