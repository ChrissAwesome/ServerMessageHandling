package MessageData;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by christian on 22.03.2016.
 */
public class MessageCont implements Serializable
{
    public String from = null;
    public String to = null;
    public Object data = null;

    //Flags
    public boolean isUTF8Text = false;
    public boolean isSoundFile = false;
    public boolean isVideoFile = false;
    public boolean isPictureFile = false;

    //PackageInformations
    public Date messageDate = new Date();
    public boolean dataIsOnPlate = false;
    public String dataPlatePath = "";


    public MessageCont(String messageFrom, String messageTo, boolean isUTF8Text , boolean isSoundFile , boolean isVideoFile, boolean isPictureFile, Object data)
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
