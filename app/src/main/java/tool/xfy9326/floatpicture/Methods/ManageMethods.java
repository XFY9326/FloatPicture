package tool.xfy9326.floatpicture.Methods;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.LinkedHashMap;

import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.Utils.PictureData;

import static tool.xfy9326.floatpicture.Methods.WindowsMethods.getWindowManager;


public class ManageMethods {

    public static void SelectPicture(Activity mActivity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        mActivity.startActivityForResult(intent, Config.REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_GET_PICTURE);
    }

    public static void RunWin(Context mContext) {
        if (PermissionMethods.checkPermission(mContext, PermissionMethods.StoragePermission)) {
            PictureData pictureData = new PictureData();
            LinkedHashMap<String, String> list = pictureData.getListArray();
            WindowManager windowManager = getWindowManager(mContext);
            if (list.size() > 0) {
                for (Object o : list.entrySet()) {
                    LinkedHashMap.Entry entry = (LinkedHashMap.Entry) o;
                    StartWin(mContext, windowManager, pictureData, entry.getKey().toString());
                }
            }
        }
    }

    private static void StartWin(Context mContext, WindowManager windowManager, PictureData pictureData, String id) {
        pictureData.setDataControl(id);
        Bitmap bitmap = ImageMethods.getShowBitmap(mContext, id);
        float zoom = pictureData.getFloat(Config.DATA_PICTURE_ZOOM, ImageMethods.getDefaultZoom(mContext, bitmap, false));
        int position_x = pictureData.getInt(Config.DATA_PICTURE_POSITION_X, Config.DATA_DEFAULT_PICTURE_POSITION_X);
        int position_y = pictureData.getInt(Config.DATA_PICTURE_POSITION_Y, Config.DATA_DEFAULT_PICTURE_POSITION_Y);
        ImageView imageView = ImageMethods.createPictureView(mContext, bitmap, zoom);
        ImageMethods.saveImageViewById(mContext, id, imageView);
        if (pictureData.getBoolean(Config.DATA_PICTURE_SHOW_ENABLED, Config.DATA_DEFAULT_PICTURE_SHOW_ENABLED)) {
            WindowsMethods.createWindow(windowManager, imageView, position_x, position_y);
        }
    }

    public static void DeleteWin(Context mContext, String id) {
        PictureData pictureData = new PictureData();
        pictureData.setDataControl(id);
        if (pictureData.getBoolean(Config.DATA_PICTURE_SHOW_ENABLED, Config.DATA_DEFAULT_PICTURE_SHOW_ENABLED)) {
            ImageView imageView = ImageMethods.getImageViewById(mContext, id);
            if (imageView != null) {
                getWindowManager(mContext).removeView(imageView);
                imageView.refreshDrawableState();
            }
        }
        pictureData.remove();
        ImageMethods.clearAllTemp(mContext, id);
    }

    static void CloseAllWindows(Context mContext) {
        HashMap<String, View> hashMap = ((MainApplication) mContext.getApplicationContext()).getRegister();
        WindowManager windowManager = getWindowManager(mContext);
        PictureData pictureData = new PictureData();
        if (hashMap.size() > 0) {
            for (Object o : hashMap.entrySet()) {
                HashMap.Entry entry = (HashMap.Entry) o;
                pictureData.setDataControl(entry.getKey().toString());
                if (pictureData.getBoolean(Config.DATA_PICTURE_SHOW_ENABLED, Config.DATA_DEFAULT_PICTURE_SHOW_ENABLED)) {
                    ImageView imageView = (ImageView) entry.getValue();
                    windowManager.removeView(imageView);
                    imageView.refreshDrawableState();
                }
            }
        }
    }

    public static void hideWindowById(Context mContext, String id) {
        ImageView imageView = ImageMethods.getImageViewById(mContext, id);
        getWindowManager(mContext).removeView(imageView);
    }

    public static void showWindowById(Context mContext, String id) {
        ImageView imageView = ImageMethods.getImageViewById(mContext, id);
        WindowManager.LayoutParams layoutParams = WindowsMethods.getDefaultLayout();
        PictureData pictureData = new PictureData();
        pictureData.setDataControl(id);
        layoutParams.x = pictureData.getInt(Config.DATA_PICTURE_POSITION_X, Config.DATA_DEFAULT_PICTURE_POSITION_X);
        layoutParams.y = pictureData.getInt(Config.DATA_PICTURE_POSITION_Y, Config.DATA_DEFAULT_PICTURE_POSITION_Y);
        getWindowManager(mContext).addView(imageView, layoutParams);
    }

}
