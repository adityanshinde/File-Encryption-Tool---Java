package encryption;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptionHandlerTest {
    @Test
    public void testEncryptDecrypt() throws Exception {
        String passphrase = "thisisaverysecurekey!";
        String original = "Hello, World!";
        byte[] encrypted = EncryptionHandler.encrypt(original.getBytes("UTF-8"), passphrase);
        assertNotNull(encrypted, "Encryption should not return null");
        byte[] decrypted = EncryptionHandler.decrypt(encrypted, passphrase);
        assertNotNull(decrypted, "Decryption should not return null");
        assertEquals(original, new String(decrypted, "UTF-8"), "Decrypted text should match original");
    }

    @Test
    public void testInvalidPassphrase() {
        String passphrase = "short";
        String original = "Test Data";
        Exception exception = assertThrows(EncryptionHandler.EncryptionException.class, () -> {
            EncryptionHandler.encrypt(original.getBytes("UTF-8"), passphrase);
        });
        assertTrue(exception.getMessage().contains("at least 16 characters"));
    }

    @Test
    public void testDecryptWithWrongPassphrase() throws Exception {
        String passphrase = "thisisaverysecurekey!";
        String wrongPass = "anotherverysecurekey!";
        String original = "Secret Message";
        byte[] encrypted = EncryptionHandler.encrypt(original.getBytes("UTF-8"), passphrase);
        assertNotNull(encrypted);
        assertThrows(EncryptionHandler.EncryptionException.class, () -> {
            EncryptionHandler.decrypt(encrypted, wrongPass);
        });
    }
}
