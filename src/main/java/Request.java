import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a request to be sent to a server. This class can be extended to include various types of data depending on
 * the application's needs.
 */
public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Ensure compatibility during serialization

    private String messageType;
    private String messageContent;
    private byte[] imageSection;

    /**
     * Constructs a new Request with specified message type and content.
     *
     * @param messageType    The type of the message, which can be used by the server to determine how to process the
     *                       request.
     * @param messageContent The content of the message.
     */
    public Request ( String messageType , String messageContent , BufferedImage imageSection ) {
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.imageSection = ImageTransformer.createBytesFromImage(imageSection);
        }

    /**
     * Returns the type of the message.
     *
     * @return The type of the message.
     */
    public String getMessageType ( ) {
        return messageType;
    }

    /**
     * Sets the type of the message.
     *
     * @param messageType The type of the message.
     */
    public void setMessageType ( String messageType ) {
        this.messageType = messageType;
    }

    /**
     * Returns the content of the message.
     *
     * @return The content of the message.
     */
    public String getMessageContent ( ) {
        return messageContent;
    }

    /**
     * Sets the content of the message.
     *
     * @param messageContent The content of the message.
     */
    public void setMessageContent ( String messageContent ) {
        this.messageContent = messageContent;
    }

    public byte[] getImageSection() {
        return imageSection;
    }

    public void setImageSection(byte[] imageSection) {
        this.imageSection = imageSection;
    }

    @Override
    public String toString ( ) {
        return "Request{" +
                "messageType='" + messageType + '\'' +
                ", messageContent='" + messageContent + '\'' +
                '}';
    }
}