package imcom.crypto.demo;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		byte[] iv = "datasecurity2012".getBytes();

		FileOperator encryptionOperator = new FileOperator("/Users/Jinyu/danid.log", "/Users/Jinyu/Documents/code/lab/cipher_text");
		
		long length = encryptionOperator.getFileLength();
		byte[] plaintext = new byte[(int) length];
		int rtn = encryptionOperator.getFileContent(plaintext);
		if (rtn == -1) {
			System.out.println("Failed to read plaintext");
			System.exit(-1);
		}
		AESCryptor cryptor = new AESCryptor();
		encryptionOperator.writeContentToFile(cryptor.encrypt(plaintext, iv));
		
		byte[] key = cryptor.getSecretKey();
		
		FileOperator decryptionOperator = new FileOperator("/Users/Jinyu/Documents/code/lab/cipher_text", "/Users/Jinyu/Documents/code/lab/plain_text");
		
		length = decryptionOperator.getFileLength();
		byte[] ciphertext = new byte[(int) length];
		rtn = decryptionOperator.getFileContent(ciphertext);
		if (rtn == -1) {
			System.out.println("Failed to read ciphertext");
			System.exit(-1);
		}
		
		decryptionOperator.writeContentToFile(cryptor.decrypt(ciphertext, key, iv));
		
		DigiSigner signer = new DigiSigner("/Users/Jinyu/imcom_keystore");
		//Digester digester = new Digester();
		
		//byte[] digest = new byte[32];
		//digest = digester.getDigest(plaintext);
		
		signer.doSignature(plaintext);
		
		//System.out.println(signer.doSignature(digest));
		
		if (signer.verifySignature("/Users/Jinyu/Documents/code/lab/signature", plaintext)) {
			System.out.println("Valid signature");
		}
		
	}

}
