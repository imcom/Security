package imcom.crypto.demo;

import java.security.SecureRandom;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;


public class AESCryptor implements Cryptor {

	private CipherKeyGenerator keyGen = new CipherKeyGenerator();
	private KeyGenerationParameters keyGenPara = null;
	private SecureRandom secRandom = null;
	private byte[] secretKey = new byte[32]; // 256-bits, 32 bytes
	private CipherParameters cipherPara = null;
	private BlockCipher blockCipher = new AESEngine();
	private PaddedBufferedBlockCipher paddedBlockCipher = null;
	private CBCBlockCipher cbcCipher = null;
	
	public AESCryptor() {
		secRandom = new SecureRandom();
		keyGenPara = new KeyGenerationParameters(secRandom, 256); // AES256, 256-bits key
		keyGen.init(keyGenPara);
	}
	
	private void initSecretKey(byte[] iv) {
		secretKey = keyGen.generateKey();
		cipherPara = new ParametersWithIV(new KeyParameter(secretKey), iv);
		cbcCipher = new CBCBlockCipher(blockCipher);
	}
	
	private void setSecretKey(byte[] key, byte[] iv) {
		this.secretKey = key;
		cipherPara = new ParametersWithIV(new KeyParameter(secretKey), iv);
		cbcCipher = new CBCBlockCipher(blockCipher);
	}
	
	public byte[] getSecretKey() {
		return secretKey;
	}
	
	private byte[] doCipher(PaddedBufferedBlockCipher aes, byte[] data) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
		int origin_length = aes.getOutputSize(data.length);
		byte[] buf = new byte[origin_length];
		int length = aes.processBytes(data, 0, data.length, buf, 0);
		length += aes.doFinal(buf, length);
		
		if (origin_length == length) {
			return buf;
		} else {
			byte[] result = new byte[length];
			System.arraycopy(buf, 0, result, 0, length);
			return result;
		}
		
	}
	
	@Override
	public byte[] encrypt(byte[] plain, byte[] iv) {

		initSecretKey(iv);
		paddedBlockCipher = new PaddedBufferedBlockCipher(cbcCipher); // default PKCS7 padding
		paddedBlockCipher.init(true, cipherPara);
		try {
			return doCipher(paddedBlockCipher, plain);
		} catch (DataLengthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public byte[] decrypt(byte[] cipher, byte[] key, byte[] iv) {

		setSecretKey(key, iv);
		paddedBlockCipher = new PaddedBufferedBlockCipher(cbcCipher); // default PKCS7 padding
		paddedBlockCipher.init(false, cipherPara);
		try {
			return doCipher(paddedBlockCipher, cipher);
		} catch (DataLengthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

}
