package imcom.crypto.demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digester {
	private MessageDigest md = null;
	
	public Digester() {
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] getDigest(byte[] message) {
		md.update(message);
		return md.digest();
	}
}
