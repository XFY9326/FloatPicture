package tool.xfy9326.floatpicture.Methods;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.Utils.PictureData;

public class WindowsMethods {
    public static WindowManager getWindowManager(Context mContext) {
        return (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

    public static void createWindow(WindowManager windowManager, View pictureView, int layoutPositionX, int layoutPositionY) {
        WindowManager.LayoutParams layoutParams = getDefaultLayout();
        layoutParams.x = layoutPositionX;
        layoutParams.y = layoutPositionY;
        windowManager.addView(pictureView, layoutParams);
    }

    private static WindowManager.LayoutParams getDefaultLayout() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return layoutParams;
    }

    public static void updateWindow(WindowManager windowManager, ImageView pictureView, Bitmap bitmap, float zoom, int layoutPositionX, int layoutPositionY) {
        WindowManager.LayoutParams layoutParams = getDefaultLayout();
        layoutParams.x = layoutPositionX;
        layoutParams.y = layoutPositionY;
        pictureView.setImageBitmap(resizeBitmap(bitmap, zoom));
        windowManager.updateViewLayout(pictureView, layoutParams);
    }

    public static ImageView createPictureView(Context mContext, Bitmap bitmap, float zoom) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(resizeBitmap(bitmap, zoom));
        //noinspection deprecation
        imageView.setBackgroundColor(mContext.getResources().getColor(R.color.colorImageViewBackground));
        return imageView;
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, float zoom) {
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
        if (image_height >= image_width) {
            if (image_height > screen_height) {
                return ((float) Math.round((screen_height / image_height) * 100f)) / 100f;
            }
        } else {
            if (image_width > screen_width) {
                return ((float) Math.round((screen_width / image_width) * 100f)) / 100f;
            }
        }
        return 1;
    }

    public static void hideWindowById(Context mContext, String id) {
        ImageView imageView = ImageMethods.getImageViewById(mContext, id);
        getWindowManager(mContext).removeView(imageView);
    }

    public static void showWindowById(Context mContext, String id) {
        ImageView imageView = ImageMethods.getImageViewById(mContext, id);
        WindowManager.LayoutParams layoutParams = getDefaultLayout();
        PictureData pictureData = new PictureData();
        pictureData.setDataControl(id);
        layoutParams.x = pictureData.getInt(Config.DATA_PICTURE_POSITION_X, Config.DATA_DEFAULT_PICTURE_POSITION_X);
        layoutParams.y = pictureData.getInt(Config.DATA_PICTURE_POSITION_Y, Config.DATA_DEFAULT_PICTURE_POSITION_Y);
        getWindowManager(mContext).addView(imageView, layoutParams);
    }

}
