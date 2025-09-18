
package encryption;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EncryptionHandler {
    private static final Logger logger = Logger.getLogger(EncryptionHandler.class.getName());


    /**
     * Encrypts the given data using AES encryption and the provided passphrase.
     * @param data The data to encrypt.
     * @param passphrase The passphrase for key generation.
     * @return The encrypted byte array.
     * @throws EncryptionException if encryption fails.
     */
    public static byte[] encrypt(byte[] data, String passphrase) throws EncryptionException {
        logger.info("Starting encryption. Data length: " + (data != null ? data.length : 0));
        try {
            SecretKey key = generateKey(passphrase);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(data);
            logger.info("Encryption successful. Output length: " + (result != null ? result.length : 0));
            return result;
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Invalid passphrase: {0}", e.getMessage());
            throw new EncryptionException("Invalid passphrase: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during encryption", e);
            throw new EncryptionException("Encryption failed: " + e.getMessage(), e);
        }
    }


    /**
     * Decrypts the given data using AES decryption and the provided passphrase.
     * @param encryptedData The encrypted data to decrypt.
     * @param passphrase The passphrase for key generation.
     * @return The decrypted byte array.
     * @throws EncryptionException if decryption fails.
     */
    public static byte[] decrypt(byte[] encryptedData, String passphrase) throws EncryptionException {
        logger.info("Starting decryption. Data length: " + (encryptedData != null ? encryptedData.length : 0));
        try {
            SecretKey key = generateKey(passphrase);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(encryptedData);
            logger.info("Decryption successful. Output length: " + (result != null ? result.length : 0));
            return result;
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Invalid passphrase: {0}", e.getMessage());
            throw new EncryptionException("Invalid passphrase: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during decryption", e);
            throw new EncryptionException("Decryption failed: " + e.getMessage(), e);
        }
    }

    private static SecretKey generateKey(String passphrase) throws Exception {
        // Ensure passphrase is 16 bytes
        byte[] keyBytes = passphrase.getBytes("UTF-8");
        if (keyBytes.length < 16) {
            throw new IllegalArgumentException("Passphrase must be at least 16 characters long.");
        }
        return new SecretKeySpec(keyBytes, 0, 16, "AES");
    }

    /**
     * Custom exception for encryption/decryption errors.
     */
    public static class EncryptionException extends Exception {
        public EncryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
