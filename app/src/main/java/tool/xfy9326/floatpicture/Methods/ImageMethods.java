package tool.xfy9326.floatpicture.Methods;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;

import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.Utils.Config;

public class ImageMethods {

    private static Bitmap getBitmapFromFile(File imageFile) {
        if (imageFile.exists() && imageFile.isFile() && imageFile.canRead()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }
        return null;
    }

    public static Bitmap getPictureById(String id) {
        return getBitmapFromFile(new File(Config.DEFAULT_PICTURE_DIR + id));
    }

    private static String getNewPictureId(File imageFile) {
        return System.currentTimeMillis() + "-" + CodeMethods.getFileMD5String(imageFile);
    }

    public static String setNewImage(File imageFile) {
        if (imageFile.exists() && imageFile.canRead() && imageFile.isFile()) {
            String id = getNewPictureId(imageFile);
            if (IOMethods.copyFile(imageFile, new File(Config.DEFAULT_PICTURE_DIR + id))) {
                return id;
            }
        }
        return null;
    }

    public static void saveImageViewById(Context mContext, String id, ImageView imageView) {
        MainApplication mainApplication = (MainApplication) mContext.getApplicationContext();
        mainApplication.registerView(id, imageView);
    }

    public static ImageView getImageViewById(Context mContext, String id) {
        MainApplication mainApplication = (MainApplication) mContext.getApplicationContext();
        return (ImageView) mainApplication.getRegisteredView(id);
    }

    public static boolean clearAllTemp(Context mContext, String id) {
        MainApplication mainApplication = (MainApplication) mContext.getApplicationContext();
        boolean result = mainApplication.unregisterView(id);
        File imageFile = new File(Config.DEFAULT_PICTURE_DIR + id);
        if (imageFile.exists()) {
            result = imageFile.delete();
        }
        return result;
    }
}
