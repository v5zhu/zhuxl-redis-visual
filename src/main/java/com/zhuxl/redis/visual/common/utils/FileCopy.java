//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCopy {
    public FileCopy() {
    }

    public static void copyFile(String oldPath, String newPath) throws Exception {
        int bytesum = 0;
        int byteread = 0;
        FileInputStream inPutStream = null;
        FileOutputStream outPutStream = null;

        try {
            inPutStream = new FileInputStream(oldPath);
            outPutStream = new FileOutputStream(newPath);
            byte[] buffer = new byte[4096];

            while ((byteread = inPutStream.read(buffer)) != -1) {
                bytesum += byteread;
                outPutStream.write(buffer, 0, byteread);
            }
        } finally {
            if (inPutStream != null) {
                inPutStream.close();
                inPutStream = null;
            }

            if (outPutStream != null) {
                outPutStream.close();
                outPutStream = null;
            }

        }

    }

    public void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0L, in.size(), out);
        } catch (IOException var16) {
            var16.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }

    }
}
