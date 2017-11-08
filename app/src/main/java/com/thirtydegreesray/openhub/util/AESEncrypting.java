

package com.thirtydegreesray.openhub.util;

import android.support.annotation.NonNull;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密
 * @author ThirtyDegreesRay
 *
 */
public class AESEncrypting {
	
	private final static String DEFAULT_KEY = "OpenHubAESKeyRay";

	/**
	 * AES加密，使用默认密钥
	 * @param input 加密字符串
	 * @return
	 */
	public static String encrypt(@NonNull String input){
		return encrypt(input, DEFAULT_KEY);
	}

	/**
	 * AES解密，使用默认密钥
	 * @param input 解密字符串
	 * @return
	 */
	public static String decrypt(String input){
		return decrypt(input, DEFAULT_KEY);
	}

	/**
	 * AES加密
	 * @param input 加密字符串
	 * @param key 密钥,密钥必须是16位的
	 * @return
	 */
	public static String encrypt(@NonNull String input, @NonNull String key){
	  byte[] crypted = null;
	  try{
		  SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	      cipher.init(Cipher.ENCRYPT_MODE, skey);
	      crypted = cipher.doFinal(input.getBytes());
	    }catch(Exception e){
	    	System.out.println(e.toString());
	    }
	    return new String(Base64.encode(crypted, Base64.DEFAULT));
	}
	
	/**
	 * AES解密
	 * @param input 解密字符串
	 * @param key 密钥,密钥必须是16位的
	 * @return
	 */
	public static String decrypt(String input, @NonNull String key){
	    byte[] output = null;
	    try{
	      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	      cipher.init(Cipher.DECRYPT_MODE, skey);
	      output = cipher.doFinal(Base64.decode(input, Base64.DEFAULT));
	    }catch(Exception e){
	      System.out.println(e.toString());
	    }
	    return new String(output);
	}

}
