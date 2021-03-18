import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.management.RuntimeErrorException;

public class FileManager {
    private String path;
    private int replication;
    private File file;
    private String fileID;

    public FileManager(String path, int replication) {
        this.path = path;
        this.replication = replication;

        this.file = new File(path);
        String tempFileID = createFileID();
        System.out.println("Temp File ID: " + tempFileID);
        this.fileID = createHash256(tempFileID);
        System.out.println("File ID: " + this.fileID);
    }

    public String createFileID() {
        String fileName = this.file.getName();
        String fileParent = this.file.getParent();
        long lastModifiedTime = this.file.lastModified();
        long fileSize = this.file.length(); 

        System.out.println("File name: " + fileName);
        System.out.println("File parent: " + fileParent);
        System.out.println("Last Modified Time: " + lastModifiedTime);
        System.out.println("File size: " + fileSize);
        
        String id = fileParent + "__" + fileName + "__" + String.valueOf(lastModifiedTime) + "__" + String.valueOf(fileSize);
        return id;
    }

    /* https://www.baeldung.com/sha-256-hashing-java */
    public String createHash256(String toHash) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
            StringBuffer buf = new StringBuffer();

            for(int i = 0; i < encodedHash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedHash[i]);
                if(hex.length() == 1) {
                    buf.append('0');
                }
                buf.append(hex);
            }
            return buf.toString();
        } catch(Exception e) {
            throw new RuntimeException(e);
        } 
    }

    /* http://all-aboutl.blogspot.com/2012/06/how-to-split-large-files-into-smaller.html */
    public void splitFile() {
        int readBytes;
        byte[] buf = new byte[64000];

        try(FileInputStream fileInputStream = new FileInputStream(this.file); BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
            while(readBytes = bufferedInputStream.read(buf)) {
                byte[] chunkMessage = Arrays.copyOf(buf, readBytes);

                // create chunk

                buf = new byte[64000];
            }

            // The maximum size of each chunks 64KByte (where K stands for 1000). All chunks of a file, except possibly the last
            // one, have the maximum size. The size of the last chunk is always shorter than that size. If the file size is a 
            // multiple of the chunk size, the last chunk has size 0
            if(this.file.length() % 64000 == 0) {
                // create the last chunk with size 0
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}