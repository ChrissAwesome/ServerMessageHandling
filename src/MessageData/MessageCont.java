package MessageData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by christian on 22.03.2016.
 */
public class MessageCont
{
    private String m_from;

    private boolean m_isUTF8Text = false;
    private boolean m_isSoundFile = false;
    private boolean m_isVideoFile = false;
    private boolean m_isPictureFile = false;

    private byte[] m_data;

    private Date messageDate = new Date();


    public MessageCont(String messageFrom, boolean isUTF8Text , boolean isSoundFile , boolean isVideoFile, boolean isPictureFile, byte[] data)
    {
        m_from = messageFrom;

        m_isUTF8Text = isUTF8Text;
        m_isSoundFile = isSoundFile;
        m_isVideoFile = isVideoFile;
        m_isPictureFile = isPictureFile;

        m_data = data;
    }
}
