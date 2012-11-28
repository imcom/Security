package imcom.crypto.demo;

public interface Cryptor {
	public byte[] encrypt(byte[] plain, byte[] iv);
	
	public byte[] decrypt(byte[] cipher, byte[] key, byte[] iv);
}
