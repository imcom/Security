package imcom.crypto.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DigiSigner {
	private static final String KEY_STORE_TYPE = "JKS";
	private static final String PASSWD = "19871010";
	private static final String ENTRY = "imcom";
	private PrivateKey privateKey = null;
	private Certificate certificate = null;
	
	public DigiSigner(String keystore) {
		Provider provider = new BouncyCastleProvider();
		Security.addProvider(provider);
		try {
			KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
			keyStore.load(new FileInputStream(keystore), PASSWD.toCharArray());
			this.privateKey = (PrivateKey) keyStore.getKey(ENTRY, PASSWD.toCharArray());
			this.certificate = keyStore.getCertificate(ENTRY);
			
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public byte[] doSignature(byte[] message) {
		try {
			Signature sig = Signature.getInstance("SHA256withRSA", "BC");
			sig.initSign(privateKey, new SecureRandom());
			sig.update(message);
			
			byte[] signature = sig.sign();
			FileOutputStream fos = new FileOutputStream("/Users/Jinyu/Documents/code/lab/signature");
			fos.write(signature);
			fos.close();
			return signature;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean verifySignature(String sig_file, byte[] message) {
		try {
			FileInputStream fis = new FileInputStream("/Users/Jinyu/Documents/code/lab/signature");
			byte[] signature = new byte[1024];
			int length = fis.read(signature);
			Signature sig = Signature.getInstance("SHA256withRSA", "BC");
			sig.initVerify(certificate);
			sig.update(message);
			return sig.verify(signature, 0, length);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
