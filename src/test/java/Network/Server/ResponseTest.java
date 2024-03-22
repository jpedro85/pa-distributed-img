package Network.Server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseTest {

    @Test
    @DisplayName("Running testConstructorAndGetters")
    public void testConstructorAndGetters() {
        String status = "Success";
        String message = "Test message";
        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

        Response response = new Response(status, message, testImage);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getImageSection());
    }

    @Test
    @DisplayName("Running testSetters")
    public void testSetters() {
        // Cria uma imagem de teste com tamanho 200x200
        BufferedImage newImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);

        // Cria uma instância de Response com status e mensagem vazios e a imagem de teste
        Response response = new Response("", "", newImage);

        // Define novos valores para o status e a mensagem
        String newStatus = "Error";
        String newMessage = "New message";
        response.setStatus(newStatus);
        response.setMessage(newMessage);

        // Verifica se os novos valores foram definidos corretamente
        assertEquals(newStatus, response.getStatus());
        assertEquals(newMessage, response.getMessage());

        // Verifica se a imagem não é nula
        assertNotNull(response.getImageSection());

        //Verifica o toString
        assertEquals(response.toString(),"Response{" + "status='" + "Error" + '\'' + ", message='" + "New message" + '\'' + '}');
    }
    @Test
    @DisplayName("Running testNullImageSection")
    public void testNullImageSection() {
        try {
            Response response = new Response("", "", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Verificando se a mensagem da exceção é a esperada
            assertEquals("image == null!", e.getMessage());
        }
    }
}
