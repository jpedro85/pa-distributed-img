package Network.Server;
import Utils.Image.ImageTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {

    @Test
    @DisplayName("Running testConstructorAndGetters")
    public void testConstructorAndGetters() {
        String messageType = "TestType";
        String messageContent = "TestContent";
        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        Request request = new Request(messageType, messageContent, testImage);

        assertEquals(messageType, request.getMessageType());
        assertEquals(messageContent, request.getMessageContent());
        assertNotNull(request.getImageSection());

        try {
            // Converte a imagem de bytes de volta para BufferedImage
            BufferedImage receivedImage = ImageIO.read(new ByteArrayInputStream(request.getImageSection()));
            assertEquals(testImage.getWidth(), receivedImage.getWidth());
            assertEquals(testImage.getHeight(), receivedImage.getHeight());
            // Você pode adicionar mais asserções aqui, se necessário
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Running testSetters")
    public void testSetters() {
        // Criando uma imagem de exemplo (10x10) para uso no teste
        BufferedImage exampleImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        // Criando uma instância de Request com uma imagem não nula
        Request request = new Request("", "", exampleImage);

        // Definindo novos valores
        String newMessageType = "NewMessageType";
        String newMessageContent = "NewMessageContent";

        request.setMessageType(newMessageType);
        request.setMessageContent(newMessageContent);

        // Garantindo que os novos valores foram definidos corretamente
        assertEquals(newMessageType, request.getMessageType());
        assertEquals(newMessageContent, request.getMessageContent());

        // Verificando se a seção de imagem não é nula
        assertNotNull(request.getImageSection());

        // Testando a conversão de bytes para imagem
        byte[] imageBytes = request.getImageSection();
        assertNotNull(imageBytes);

        // Convertendo bytes de imagem de volta para BufferedImage
        BufferedImage receivedImage = ImageTransformer.createImageFromBytes(imageBytes);
        assertNotNull(receivedImage);

        // Verificando se as dimensões da imagem original e da imagem recebida são iguais
        assertEquals(exampleImage.getWidth(), receivedImage.getWidth());
        assertEquals(exampleImage.getHeight(), receivedImage.getHeight());

        //Verifica o toString
        assertEquals(request.toString(),"Request{" +
                "messageType='" + "NewMessageType" + '\'' +
                ", messageContent='" + "NewMessageContent" + '\'' +
                '}');
    }


    @Test
    @DisplayName("Running testNullImageSection")
    public void testNullImageSection() {
        try {
            Request request = new Request("", "", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Verificando se a mensagem da exceção é a esperada
            assertEquals("image == null!", e.getMessage());
        }
    }



}
