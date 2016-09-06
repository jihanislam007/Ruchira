package com.techcoderz.ruchira.utills;

import java.io.InputStream;
import java.io.OutputStream;

class Utils {
    public static void CopyStream(InputStream inputStream, OutputStream outputStream) {
        final int bufferSize = 1024;

        try {
            byte[] bytes = new byte[bufferSize];
            for (; ; ) {
                //Read byte from input stream
                int count = inputStream.read(bytes, 0, bufferSize);
                if (count == -1)
                    break;

                //Write byte from output stream
                outputStream.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }
}
