package mcstorage.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGen {
	public static String generateStringHash(String string) {
		
		return digestIt(string.getBytes());		
		
	}
	
	public static String generateISHash(InputStream is) {
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			
			byte[] buffer = new byte[1024];
			int bytesRead;
		
			while((bytesRead = is.read(buffer)) != -1) {
			md.update(buffer, 0, bytesRead);
			
			byte[] d = md.digest();
			
			StringBuilder hexString = new StringBuilder();
      for (byte b : d) {
          hexString.append(String.format("%02x", b));
      }

      return hexString.toString();
		}
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO: handle exception
		}
		
		return "NOHASHLOL";
	}
	
	private static String digestIt(byte[] bytes) {
		byte[] hash;
		MessageDigest md;
		try{
			md = MessageDigest.getInstance("SHA-256");
			
			hash = md.digest(bytes);
			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
			return "NOHASHLOL";
		}
		StringBuilder outputString = new StringBuilder("");
		for(int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1 ) {
				outputString.append('0');
			}
			outputString.append(hex);
		}
		return outputString.toString();
	}
}
