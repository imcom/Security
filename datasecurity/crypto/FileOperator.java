package imcom.crypto.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOperator {
	
	private File ifile, ofile;

	public FileOperator(String input_file, String output_file) {
		this.ifile = new File(input_file);
		this.ofile = new File(output_file);
	}
	
	public long getFileLength() {
		return ifile.length();
	}
	
	public void writeContentToFile(byte[] content) {
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(this.ofile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (fos != null) {
			try {
				fos.write(content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return;
	}
	
	public int getFileContent(byte[] content) {
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(this.ifile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (fis != null) {
			try {
				return fis.read(content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return 0;
		
	}
}
