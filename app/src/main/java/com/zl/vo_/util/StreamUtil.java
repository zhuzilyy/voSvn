package com.zl.vo_.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lenovo on 2017/9/13.
 */

public class StreamUtil {
    public StreamUtil() {
    }

    public static void close(Closeable... closeables) {
        if(closeables != null && closeables.length != 0) {
            Closeable[] var1 = closeables;
            int var2 = closeables.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Closeable closeable = var1[var3];
                if(closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException var6) {
                        var6.printStackTrace();
                    }
                }
            }

        }
    }

    public static boolean copyFile(File srcFile, File saveFile) {
        File parentFile = saveFile.getParentFile();
        if(!parentFile.exists() && !parentFile.mkdirs()) {
            return false;
        } else {
            BufferedInputStream inputStream = null;
            BufferedOutputStream outputStream = null;

            boolean len;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(srcFile));
                outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));
                byte[] e = new byte[4096];

                int len1;
                while((len1 = inputStream.read(e)) != -1) {
                    outputStream.write(e, 0, len1);
                }

                outputStream.flush();
                return true;
            } catch (IOException var10) {
                var10.printStackTrace();
                len = false;
            } finally {
                close(new Closeable[]{inputStream, outputStream});
            }

            return len;
        }
    }
}

