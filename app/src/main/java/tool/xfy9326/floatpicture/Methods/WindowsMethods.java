package tool.xfy9326.floatpicture.Methods;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.View.FloatImageView;


public class WindowsMethods {
    public static WindowManager getWindowManager(Context mContext) {
        return (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

    @SuppressWarnings("SameParameterValue")
    public static void createWindow(WindowManager windowManager, View pictureView, boolean touchable, boolean overLayout, int layoutPositionX, int layoutPositionY) {
        WindowManager.LayoutParams layoutParams = getDefaultLayout(pictureView.getContext(), layoutPositionX, layoutPositionY, touchable, overLayout);
        windowManager.addView(pictureView, layoutParams);
    }

    public static WindowManager.LayoutParams getDefaultLayout(Context context, int layoutPositionX, int layoutPositionY, boolean touchable, boolean overLayout) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (overLayout) {
            layoutParams.flags = layoutParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        } else {
            layoutParams.flags = layoutParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        }
        if (!touchable) {
            layoutParams.flags = layoutParams.flags | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }
        layoutParams.x = layoutPositionX;
        layoutParams.y = layoutPositionY;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            layoutParams.alpha = ((MainApplication) context.getApplicationContext()).getSafeWindowsAlpha();
        }
        return layoutParams;
    }

    public static void updateWindow(WindowManager windowManager, FloatImageView pictureView, boolean touchable, boolean overLayout, int layoutPositionX, int layoutPositionY) {
        WindowManager.LayoutParams layoutParams = getDefaultLayout(pictureView.getContext(), layoutPositionX, layoutPositionY, touchable, overLayout);
        windowManager.updateViewLayout(pictureView, layoutParams);
    }

    public static void updateWindow(WindowManager windowManager, FloatImageView pictureView, Bitmap bitmap, boolean touchable, boolean overLayout, float zoom, float degree, int layoutPositionX, int layoutPositionY) {
        pictureView.refreshDrawableState();
        pictureView.setImageBitmap(ImageMethods.resizeBitmap(bitmap, zoom, degree));
        updateWindow(windowManager, pictureView, touchable, overLayout, layoutPositionX, layoutPositionY);
    }
}
