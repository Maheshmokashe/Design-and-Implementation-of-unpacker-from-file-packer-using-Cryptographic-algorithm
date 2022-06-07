import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryption 
{

	File fileInput = null, fileOutput = null;
	FileInputStream inStream = null;
	FileOutputStream outStream = null;

	public FileEncryption(String fileToBeEncrypted) throws Exception 
	{

		FilePackerUnpacker.log.info("File Encryption started..");
		// First create an AES Key
		String secretAESKeyString = getSecretAESKeyAsString();

		// encryptedFile
		SecretKeySpec key = new SecretKeySpec(secretAESKeyString.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		fileInput = new File(fileToBeEncrypted);
		inStream = new FileInputStream(fileInput);
		byte[] inputBytes = new byte[(int) fileInput.length()];
		inStream.read(inputBytes);
		byte[] outputBytes = cipher.doFinal(inputBytes);
		fileOutput = new File(fileToBeEncrypted);
		outStream = new FileOutputStream(fileOutput);
		byte[] br = secretAESKeyString.getBytes();
		outStream.write(br);
		outStream.write(outputBytes);

		// Close opened resources
		inStream.close();
		outStream.close();
		FilePackerUnpacker.log.info("File Encryption completed..");
	}

	// Create a new AES key. Uses 128 bit (weak)
	public static String getSecretAESKeyAsString() throws Exception 
	{
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128); // The AES key size in number of bits
		SecretKey secKey = generator.generateKey();
		String encodedKey = Base64.getEncoder().encodeToString(secKey.getEncoded());
		return encodedKey;
	}
}
