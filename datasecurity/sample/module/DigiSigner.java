package sample.module;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

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

            InputStream is = new FileInputStream("/home/imcom/Lab/workspace/JAAS/imcom.cer");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            this.certificate = cf.generateCertificate(is);

            is.close();

		} catch (Exception e) {
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
			byte[] signature_base64 = Base64.encode(signature);
			FileOutputStream fos = new FileOutputStream("/home/imcom/Lab/workspace/JAAS/passwd.sig");
			fos.write(signature_base64);
			fos.close();
			return signature_base64;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean verifySignature(String sig_file, byte[] message) {
		try {
			FileInputStream fis = new FileInputStream(sig_file);
			byte[] buffer = new byte[1024];
			int length = fis.read(buffer);
			String signature_base64 = new String(buffer, 0, length);
			byte[] signature = Base64.decode(signature_base64);
			Signature sig = Signature.getInstance("SHA256withRSA", "BC");
			sig.initVerify(certificate);
			sig.update(message);
			fis.close();
			return sig.verify(signature, 0, signature.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

    public static void main(String args[]) {
        DigiSigner signer = new DigiSigner("/home/imcom/Lab/workspace/JAAS/imcom_keystore");
        FileOperator fopr = new FileOperator("/home/imcom/Lab/workspace/JAAS/jaaspasswd");
        byte[] content = new byte[(int)fopr.getFileLength()];
        fopr.getFileContent(content);
        byte[] signature = signer.doSignature(content);
        System.out.println("Signature generated: " + new String(signature));
    }

}
