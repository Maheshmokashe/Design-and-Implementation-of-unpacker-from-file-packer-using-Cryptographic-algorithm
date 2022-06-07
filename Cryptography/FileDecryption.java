import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FileDecryption 
{

	String secretAESKeyString = null;
	FileInputStream inStream = null;
	FileOutputStream outStream = null;
	File fileInput = null, fileOutput = null;

	public FileDecryption(String packedEncFile) throws Exception 
	{
		FilePackerUnpacker.log.info("File Decryption started..");
		fileInput = new File(packedEncFile);
		inStream = new FileInputStream(fileInput);
		byte[] keyReader = new byte[24];
		inStream.read(keyReader);
		secretAESKeyString = new String(keyReader);

		Map<String, Object> keys = getRSAKeys();
		PrivateKey privateKey = (PrivateKey) keys.get("private");
		PublicKey publicKey = (PublicKey) keys.get("public");

		// Encrypt AES Key with RSA Private Key
		String encryptedAESKeyString = encryptAESKey(secretAESKeyString, privateKey);

		// First Decrypt the AES Key with RSA Public key
		String decryptedAESKeyString = decryptAESKey(encryptedAESKeyString, publicKey);

		// File Decryption
		SecretKeySpec key = new SecretKeySpec(decryptedAESKeyString.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] inputBytes = new byte[(int) fileInput.length() - 24];
		inStream.read(inputBytes);
		byte[] outputBytes = cipher.doFinal(inputBytes);
		fileOutput = new File(packedEncFile);
		outStream = new FileOutputStream(fileOutput);
		outStream.write(outputBytes);

		inStream.close();
		outStream.close();
		FilePackerUnpacker.log.info("File Decryption completed..");
	}

	// Decrypt AES Key using RSA public key
	private static String decryptAESKey(String encryptedAESKey, PublicKey publicKey) throws Exception 
	{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedAESKey)));
	}

	// Encrypt AES Key using RSA private key
	private static String encryptAESKey(String plainAESKey, PrivateKey privateKey) throws Exception 
	{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(plainAESKey.getBytes()));
	}

	private static Map<String, Object> getRSAKeys() throws Exception 
	{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		Map<String, Object> keys = new HashMap<String, Object>();
		keys.put("private", privateKey);
		keys.put("public", publicKey);
		return keys;
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
