package com.blm.qiubopay.utils;

import android.util.Log;

import com.blm.qiubopay.tools.Tools;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;*/

/**
 * @author apiedra MIT
 * @version 1.0
 * 
 */
public class AESEncryption {

	private static final String ALGORITMO = "AES/CBC/PKCS5Padding";
	private static final String CODIFICACION = "UTF-8";
	private static final int IV_SIZE_BYTES = 128 / 8;

	private static final String TRANSF = "AES";
	private static final byte[] keyValue = new byte[]{'V', '1', '5', 'A', 'c', '1', 'p', 'h', 'e', 'r', 't', 'x', '2', '0', '2', '0'};

	/**
	 * Permite encriptar una cadena a partir de un llave proporcionada
	 * 
	 * @param plaintext
	 * @param key
	 * @return String con la cadena encriptada
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 * //@throws DecoderException
	 */
	/*public static String encrypt(String plaintext, String key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, DecoderException {

		byte[] raw = DatatypeConverter.parseHexBinary(key);

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance(ALGORITMO);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] cipherText = cipher.doFinal(plaintext.getBytes(CODIFICACION));
		byte[] iv = cipher.getIV();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(iv);
		outputStream.write(cipherText);

		byte[] finalData = outputStream.toByteArray();

		String encodedFinalData = DatatypeConverter.printBase64Binary(finalData);

		return encodedFinalData;
	}*/

	/**
	 * Permite desencriptar una cadena a partir de la llave proporcionada
	 * 
	 * @param encodedInitialData
	 * @param key
	 * @return String de la cadena en claro
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * //@throws DecoderException
	 */
	/*public static String decrypt(String encodedInitialData, String key)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, DecoderException {

		byte[] encryptedData = DatatypeConverter.parseBase64Binary(encodedInitialData);

		byte[] raw = DatatypeConverter.parseHexBinary(key);

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance(ALGORITMO);

		byte[] iv = Arrays.copyOfRange(encryptedData, 0, 16);

		byte[] cipherText = Arrays.copyOfRange(encryptedData, 16, encryptedData.length);
		IvParameterSpec iv_specs = new IvParameterSpec(iv);

		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv_specs);

		byte[] plainTextBytes = cipher.doFinal(cipherText);
		String plainText = new String(plainTextBytes);

		return plainText;
	}

	public static String generateIV() {
		byte[] iv = new byte[IV_SIZE_BYTES];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		char[] b = Hex.encodeHex(ivParameterSpec.getIV());
		String ivStr = String.valueOf(b);
		return ivStr;
	}*/

	public static String encrypt(String keySource, String gId) {
		try
		{
			String secretKey1 = keySource.substring(0, 32);
			String initVector = keySource.substring(32,64);
			IvParameterSpec ivspec = new IvParameterSpec(Tools.hexStringToByteArray(initVector));
			SecretKeySpec secretKey = new SecretKeySpec(Tools.hexStringToByteArray(secretKey1), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			byte[] encrypted = cipher.doFinal(gId.getBytes());
			System.err.println("Encrypted: " + new String(Base64.encodeBase64(encrypted)));
			return new String(Base64.encodeBase64(encrypted));
		}
		catch (Exception e) {
			//Utilities.showLog("Error while ecrypting: " + e.toString());
			Log.d("Error al cifrar", e.toString());
		}
		return null;
	}

	public static String encrypt(String gId) {
		try
		{
			Key key = generateKey();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);//secretKey, ivspec);
			byte[] encrypted = cipher.doFinal(gId.getBytes());
			System.err.println("Encrypted: " + new String(Base64.encodeBase64(encrypted)));
			return new String(Base64.encodeBase64(encrypted));
		}
		catch (Exception e) {
			//Utilities.showLog("Error while ecrypting: " + e.toString());
			Log.d("Error al cifrar", e.toString());
		}
		return null;
	}

	private static Key generateKey() throws Exception {
		return new SecretKeySpec(keyValue, TRANSF);
	}

	/*public static void main(String args[]) throws Exception {
		String encryptionKey = "A2832DE3C0B2289253D4B383404E8C1C";
		String requestStr = "7pH3ZC8hIoXsCJ/enNZn4akA1Y2inLRqxgNrbvIiVSouxWGsNxcsn81HboERCjDiP0ZrtXuqTiQ26YMF1nEfa2DPFM7WaSSzccrjOB5Er6rXvl445LXsLlXu2Pw9mWewva2uRVao8LTWoOffZQ9ZwZE+vNWl+uuRq5XEA3l8OhXZ6DSPunIpHm96+T/G/KjU0uPXkqlssAsr+6CwswWNlTI7J7zXDD/u4gu9z28HM9hK3GQi+EXXDYyh54qL1IJWXxfjTzNPKQp7QVkjm1sKMCQdn3qdPTX4BFDauV3WzdYgelSk0KoF5z8baz2GT4QEgYYGj9YUaXgQHPGPvS8sHsRMIAzHpzduT2b1B9zVkY9jobiCFvCAjljc9vfkHWG5JJTh8N8Y4fsc59Zhcv2r+vxeXrGU+SPW6yIcvu9cs7Eh8cO1ZWslItucVrUwDDMfR/AJGhyDVfnn978sw8o3OOrpahfON37OP4DDNWhM3a56qQNWb7dSONTXlLeG8G+P1751gaDGHNd15+uXMH1fV4Qnwqd7otiy9vwLLyEbSyFRSg+j2wWz3lMWyawHVV4JsuRVWVbUqrVLmqkpgoMfMePQ9gwLZ7DbhwE6evvLhuKTTekKv3h+zI5OE6a0Uhjq/h0+jUlsGxM7UBOX30MJOQIL9pLy9v4ZZCwH+V9fJUQPO+Yae0xInAwGhwDBRVYlA/sJIsbbA3KzNe/BGOnxkhvgKCwBCKN1gQLKqRFPdEaFWPLBGjjTiMSgobwNOAWfqexuq92AWYZ6zq8pesykE9GlhvTes/SNF7yc9DRe759nHMMacrgbADeYPvPN1MQr5eHTDyD/XPh+t3yLOPWoZJfliBcP/5lohLO4CXIzfwJt7RFP+8kIAAzA4Y875d2qhZ/q1hUsgZiv4vaSkfexO9yNSwWdIhgGCMSMXGWK9/A=";
		String clearedData = AESEncryption.decrypt(requestStr, encryptionKey);
		System.out.println(clearedData);
	}*/

}
