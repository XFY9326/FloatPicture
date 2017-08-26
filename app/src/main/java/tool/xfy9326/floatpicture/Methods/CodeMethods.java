package tool.xfy9326.floatpicture.Methods;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class CodeMethods {

    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String unicodeEncode(String str) {
        if (str != null) {
            StringBuilder unicode = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                unicode.append("\\u").append(Integer.toHexString(c));
            }
            str = unicode.toString();
        }
        return str;
    }

    public static String unicodeDecode(String str) {
        if (str != null) {
            StringBuilder string = new StringBuilder();
            String[] hex = str.split("\\\\u");
            for (int i = 1; i < hex.length; i++) {
                int data = Integer.parseInt(hex[i], 16);
                string.append((char) data);
            }
            str = string.toString();
        }
        return str;
    }

    private static String getFileMD5String(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(byteBuffer);
            return bufferToHex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bufferToHex(byte bytes[]) {
        StringBuffer stringbuffer = new StringBuffer(2 * bytes.length);
        for (byte b : bytes) {
            appendHexPair(b, stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

}
