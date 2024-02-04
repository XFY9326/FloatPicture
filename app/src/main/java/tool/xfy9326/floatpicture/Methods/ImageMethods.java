package tool.xfy9326.floatpicture.Methods;

import static android.graphics.Bitmap.createBitmap;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.DisplayMetrics;

import androidx.exifinterface.media.ExifInterface;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.View.FloatImageView;

public class ImageMethods {

    private static Bitmap getBitmapFromFile(File imageFile) {
        if (imageFile.exists() && imageFile.isFile() && imageFile.canRead()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }
        return null;
    }

    private static Bitmap getPictureById(String id) {
        return getBitmapFromFile(new File(Config.DEFAULT_PICTURE_DIR + id));
    }

    private static Bitmap getPictureTempById(String id) {
        return getBitmapFromFile(new File(Config.DEFAULT_PICTURE_TEMP_DIR + id));
    }

    private static String getNewPictureId(Context mContext, Uri uri) {
        return System.currentTimeMillis() + "-" + CodeMethods.getFileMD5String(mContext, uri);
    }

    public static String setNewImage(Context mContext, Uri uri) {
        try {
            String id = getNewPictureId(mContext, uri);
            Bitmap bitmap = getNewBitmap(mContext, uri);
            IOMethods.saveBitmap(bitmap, PreferenceManager.getDefaultSharedPreferences(mContext).getInt(Config.PREFERENCE_NEW_PICTURE_QUALITY, 80), Config.DEFAULT_PICTURE_DIR + id);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveFloatImageViewById(Context mContext, String id, FloatImageView FloatImageView) {
        MainApplication mainApplication = (MainApplication) mContext.getApplicationContext();
        mainApplication.registerView(id, FloatImageView);
    }

    public static FloatImageView getFloatImageViewById(Context mContext, String id) {
        MainApplication mainApplication = (MainApplication) mContext.getApplicationContext();
        return (FloatImageView) mainApplication.getRegisteredView(id);
    }

    public static FloatImageView createPictureView(Context mContext, Bitmap bitmap, boolean touchable, boolean overLayout, float zoom, float degree) {
        FloatImageView imageView = new FloatImageView(mContext);
        imageView.setMoveable(touchable);
        imageView.setOverLayout(overLayout);
        imageView.setImageBitmap(resizeBitmap(bitmap, zoom, degree));
        imageView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        imageView.getBackground().setAlpha(0);
        return imageView;
    }

    public static Bitmap getEditBitmap(Context mContext, Bitmap bitmap) {
        return getEditBitmap(mContext, bitmap.getWidth(), bitmap.getHeight());
    }

    private static Bitmap getEditBitmap(Context mContext, int width, int height) {
        Bitmap transparent_bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transparent_bitmap);
        canvas.drawColor(mContext.getResources().getColor(R.color.colorImageViewEditBackground));
        return transparent_bitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, float zoom, float degree) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        if (zoom != 0) {
            matrix.postScale(zoom, zoom);
        }
        if (degree != -1) {
            matrix.postRotate(degree);
        }
        synchronized (ImageMethods.class) {
            return createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
    }

    private static Bitmap getNewBitmap(Context mContext, Uri uri) {
        int degree = 0;
        Bitmap bitmap = IOMethods.readImageByUri(mContext, uri);
        if (bitmap != null) {
            try {
                ContentResolver contentResolver = mContext.getContentResolver();
                ExifInterface exifInterface = new ExifInterface(Objects.requireNonNull(contentResolver.openAssetFileDescriptor(uri, "r")).createInputStream());
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90;
                    case ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180;
                    case ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270;
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(degree);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
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
            screen_width = displayMetrics.widthPixels / 3.0f;
            screen_height = displayMetrics.heightPixels / 3.0f;
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

    public static Bitmap getPreviewBitmap(Context mContext, String id) {
        Bitmap temp = getPictureTempById(id);
        if (temp == null) {
            Bitmap bitmap = getPictureById(id);
            if (bitmap == null) {
                temp = getEditBitmap(mContext, 50, 50);
            } else {
                IOMethods.saveBitmap(bitmap, 50, Config.DEFAULT_PICTURE_TEMP_DIR + id);
                bitmap.recycle();
                temp = getPictureTempById(id);
            }
        }
        return temp;
    }

    public static Bitmap getShowBitmap(Context mContext, String id) {
        Bitmap temp = getPictureById(id);
        if (temp == null) {
            Bitmap bitmap = getPictureTempById(id);
            if (bitmap == null) {
                temp = getEditBitmap(mContext, 50, 50);
            } else {
                temp = getEditBitmap(mContext, bitmap);
                bitmap.recycle();
            }
        }
        return temp;
    }

    public static boolean isPictureFileExist(String id) {
        File picture = new File(Config.DEFAULT_PICTURE_DIR + id);
        return picture.exists();
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
