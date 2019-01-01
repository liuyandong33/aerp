package dream.build.aerp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import dream.build.aerp.constants.Constants;

public class IOUtils {
    public static String toString(InputStream inputStream) {
        return toString(inputStream, Constants.CHARSET_NAME_UTF_8, false);
    }

    public static String toString(InputStream inputStream, String charsetName) {
        return toString(inputStream, charsetName, false);
    }

    public static String toString(InputStream inputStream, boolean closed) {
        return toString(inputStream, Constants.CHARSET_NAME_UTF_8, closed);
    }

    public static String toString(InputStream inputStream, String charsetName, boolean closed) {
        try {
            StringBuffer result = new StringBuffer();
            Reader reader = new InputStreamReader(inputStream, charsetName);
            int length = -1;
            char[] buffer = new char[1024];
            while ((length = reader.read(buffer, 0, 1024)) != -1) {
                result.append(buffer, 0, length);
            }
            reader.close();
            if (closed) {
                inputStream.close();
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void copy(InputStream inputStream, OutputStream outputStream, boolean closed) throws IOException {
        int length = -1;
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        if (closed) {
            inputStream.close();
            outputStream.close();
        }
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(inputStream, outputStream, false);
    }

    public static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
            }
        }
    }
}
