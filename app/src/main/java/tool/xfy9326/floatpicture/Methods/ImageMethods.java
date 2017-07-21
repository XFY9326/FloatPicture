package tool.xfy9326.floatpicture.Methods;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.io.File;

import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.R;
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

    private static Bitmap getPictureTempById(String id) {
        return getBitmapFromFile(new File(Config.DEFAULT_PICTURE_TEMP_DIR + id));
    }

    private static String getNewPictureId(File imageFile) {
        return System.currentTimeMillis() + "-" + CodeMethods.getFileMD5String(imageFile);
    }

    public static String setNewImage(Context mContext, File imageFile) {
        if (imageFile.exists() && imageFile.canRead() && imageFile.isFile()) {
            String id = getNewPictureId(imageFile);
            Bitmap bitmap = getBitmapFromFile(imageFile);
            IOMethods.saveBitmap(bitmap, PreferenceManager.getDefaultSharedPreferences(mContext).getInt(Config.PREFERENCE_NEW_PICTURE_QUALITY, 80), Config.DEFAULT_PICTURE_DIR + id);
            return id;
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

    public static ImageView createPictureView(Context mContext, Bitmap bitmap, float zoom) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(resizeBitmap(bitmap, zoom));
        //noinspection deprecation
        imageView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        imageView.getBackground().setAlpha(0);
        return imageView;
    }

    public static Bitmap getEditBitmap(Context mContext, Bitmap bitmap) {
        Bitmap transparent_bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transparent_bitmap);
        //noinspection deprecation
        canvas.drawColor(mContext.getResources().getColor(R.color.colorImageViewEditBackground));
        return transparent_bitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, float zoom) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(zoom, zoom);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static float getDefaultZoom(Context mContext, Bitmap bitmap, boolean isMax) {
        float image_width = bitmap.getWidth();
        float image_height = bitmap.getHeight();
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float screen_width;
        float screen_height;
        if (isMax) {
            screen_width = displayMetrics.widthPixels;
            screen_height = displayMetrics.heightPixels;
        } else {
            screen_width = displayMetrics.widthPixels / 3;
            screen_height = displayMetrics.heightPixels / 3;
        }
        if (image_height <= image_width) {
            if (image_height > screen_height || isMax) {
                return ((float) Math.round((screen_height / image_height) * 100f)) / 100f;
            }
        } else {
            if (image_width > screen_width || isMax) {
                return ((float) Math.round((screen_width / image_width) * 100f)) / 100f;
            }
        }
        return 1;
    }

    public static Bitmap getPreviewBitmap(String id) {
        Bitmap temp = getPictureTempById(id);
        if (temp == null) {
            Bitmap bitmap = getPictureById(id);
            IOMethods.saveBitmap(bitmap, 50, Config.DEFAULT_PICTURE_TEMP_DIR + id);
            temp = getPictureTempById(id);
        }
        return temp;
    }

    public static void clearAllTemp(Context mContext, String id) {
        MainApplication mainApplication = (MainApplication) mContext.getApplicationContext();
        mainApplication.unregisterView(id);
        File imageFile = new File(Config.DEFAULT_PICTURE_DIR + id);
        File tempFile = new File(Config.DEFAULT_PICTURE_TEMP_DIR + id);
        if (imageFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            imageFile.delete();
        }
        if (tempFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            tempFile.delete();
        }
    }
}
