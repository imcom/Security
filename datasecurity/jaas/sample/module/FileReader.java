package sample.module;

import java.io.*;

class FileReader {

    private FileInputStream fis = null;
    private DataInputStream dis = null;
    private BufferedReader br = null;

    public FileReader(String file_path) {
        try {
            fis = new FileInputStream(file_path);
            dis = new DataInputStream(fis);
            br = new BufferedReader(new InputStreamReader(dis));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String readLine() {
        String rtn = null;
        try {
            rtn = this.br.readLine();
            if (rtn == null) {
                dis.close();
                fis.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rtn;
    }

/*
    public static void main(String args[]) {
        FileReader fr = new FileReader("users");
        String line;
        String secret[];
        while( (line = fr.readLine()) != null ) {
            secret = line.split(":", 2);
            System.out.println(secret[0] + ", " + secret[1]);
        }
    }
*/
}
