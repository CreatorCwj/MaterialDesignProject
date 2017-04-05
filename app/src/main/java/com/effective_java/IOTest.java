package com.effective_java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

/**
 * Created by cwj on 17/4/28.
 */

public class IOTest {

    public static void main(String[] args) {
        File file = new File("testIo.txt");
        boolean hasFile = true;
        if (!file.exists()) {
            try {
                hasFile = file.createNewFile();
            } catch (IOException e) {
                hasFile = false;
            }
        }
        if (!hasFile) {
            System.out.println("no file");
            return;
        }
        writeFile(file);
        readFileByByte(file);
        readFileByChar(file);
    }

    private static void readFileByChar(File file) {
        System.out.println("read by char");
        Reader reader = null;
        try {
            reader = new FileReader(file);
            int tmp;
            int count = 0;
            while ((tmp = reader.read()) != -1) {
                ++count;
                System.out.print(tmp + " : " + (char) tmp + ",");
            }
            System.out.println("");
            System.out.println("count:" + count);
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void readFileByByte(File file) {
        System.out.println("read by byte:");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            System.out.println("available:" + inputStream.available());
            int tmp;
            while ((tmp = inputStream.read()) != -1) {
                System.out.print((byte) tmp + ",");
            }
            System.out.println("");
        } catch (IOException e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void writeFile(File file) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            byte[] bytes = "I am 一个好 man!!!".getBytes();
            for (byte b : bytes) {
                System.out.print(b + ",");
            }
            System.out.println("");
            System.out.println("length:" + bytes.length);
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
