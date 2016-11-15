package net.senmori.simpleprotect.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    public static void copyFile(InputStream in, File outFile) {
        try(OutputStream out = new FileOutputStream(outFile)) {
            byte[] buffer = new byte[1024];
            int r;
            
            while((r = in.read(buffer)) != -1) {
                out.write(buffer, 0, r);
            }
            out.close();
            in.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
