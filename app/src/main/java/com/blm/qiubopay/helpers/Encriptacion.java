package com.blm.qiubopay.helpers;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encriptacion {

	public static final String LLAVE = "Cualquier sociedad que renuncie.";

	public static String Encrypt(String plainText, String key){

		if(plainText == null || plainText.equals(""))
			return "";

		try{
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

			return Base64.encode(encryptedTextBytes);

		}catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String Decrypt(String encryptedText, String key) {


		if(encryptedText == null || encryptedText.equals(""))
			return "";

		try{
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			// Instantiate the cipher
			//Cipher cipher = Cipher.getInstance("AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);

			byte[] encryptedTextBytes = Base64.decode(encryptedText);
			byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

			return new String(decryptedTextBytes);

		}catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

}
