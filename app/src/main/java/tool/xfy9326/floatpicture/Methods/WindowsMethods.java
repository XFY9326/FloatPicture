package tool.xfy9326.floatpicture.Methods;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

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

    static WindowManager.LayoutParams getDefaultLayout() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        return layoutParams;
    }

    public static void updateWindow(WindowManager windowManager, ImageView pictureView, int layoutPositionX, int layoutPositionY) {
        WindowManager.LayoutParams layoutParams = getDefaultLayout();
        layoutParams.x = layoutPositionX;
        layoutParams.y = layoutPositionY;
        windowManager.updateViewLayout(pictureView, layoutParams);
    }

    public static void updateWindow(WindowManager windowManager, ImageView pictureView, Bitmap bitmap, float zoom, float degree, int layoutPositionX, int layoutPositionY) {
        pictureView.refreshDrawableState();
        pictureView.setImageBitmap(ImageMethods.resizeBitmap(bitmap, zoom, degree));
        updateWindow(windowManager, pictureView, layoutPositionX, layoutPositionY);
    }
}
