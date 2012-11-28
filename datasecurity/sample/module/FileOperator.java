package sample.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileOperator {
	
	private File ifile;

	public FileOperator(String input_file) {
		this.ifile = new File(input_file);
	}
	
	public long getFileLength() {
		return ifile.length();
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
