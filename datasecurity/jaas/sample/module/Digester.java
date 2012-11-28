package sample.module;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digester {
	private MessageDigest md_sha = null;
	private MessageDigest md5 = null;
	
	public Digester() {
		try {
			md_sha = MessageDigest.getInstance("SHA-256");
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public boolean verifyDigests(byte[] digest_1, byte[] digest_2) {
        return MessageDigest.isEqual(digest_1, digest_2);
    }
	
	public byte[] getSHADigest(byte[] message) {
		md_sha.update(message);
		return md_sha.digest();
	}

    public byte[] getMD5Digest(byte[] message) {
        md5.update(message);
        return md5.digest();
    }

}
