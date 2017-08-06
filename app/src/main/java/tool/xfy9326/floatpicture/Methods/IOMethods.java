package tool.xfy9326.floatpicture.Methods;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import tool.xfy9326.floatpicture.Utils.Config;

public class IOMethods {

    @SuppressWarnings("SameParameterValue")
    static void saveBitmap(Bitmap bitmap, int quality, String path) {
        File file = new File(path);
        try {
            if (!CheckFile(file, true)) {
                OutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream);
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readAssetText(Context mContext, String path) {
        try {
            String result = "";
            InputStreamReader inputReader = new InputStreamReader(mContext.getResources().getAssets().open(path));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                result += line + "\n";
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean setNoMedia() {
        File nomedia = new File(Config.NO_MEDIA_FILE_DIR);
        if (!nomedia.exists()) {
            try {
                return nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    private static boolean createPath(File file) {
        if (file.getParent().trim().length() != 1) {
            File filepath = file.getParentFile();
            if (!filepath.exists()) {
                return filepath.mkdirs();
            }
        }
        return true;
    }

    private static boolean CheckFile(File file, boolean delete) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                if (delete) {
                    if (file.delete()) {
                        if (file.createNewFile()) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        } else {
            if (!createPath(file)) {
                return true;
            }
            if (file.createNewFile()) {
                return false;
            }
        }
        return true;
    }

    public static boolean writeFile(String content, String path) {
        File file = new File(path);
        try {
            if (CheckFile(file, false)) {
                return false;
            }
            OutputStream writer = new FileOutputStream(file);
            byte[] Bytes = content.getBytes();
            writer.write(Bytes);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readFile(String path) {
        File file = new File(path);
        try {
            if (CheckFile(file, false)) {
                return null;
            }
            InputStream file_stream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file_stream));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            reader.close();
            file_stream.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
