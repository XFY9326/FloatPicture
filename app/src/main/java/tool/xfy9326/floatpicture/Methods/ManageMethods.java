package tool.xfy9326.floatpicture.Methods;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.LinkedHashMap;

import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.Utils.PictureData;

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
            WindowManager windowManager = WindowsMethods.getWindowManager(mContext);
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
        Bitmap bitmap = ImageMethods.getPictureById(id);
        float zoom = pictureData.getFloat(Config.DATA_PICTURE_ZOOM, WindowsMethods.getDefaultZoom(mContext, bitmap, false));
        int position_x = pictureData.getInt(Config.DATA_PICTURE_POSITION_X, Config.DATA_DEFAULT_PICTURE_POSITION_X);
        int position_y = pictureData.getInt(Config.DATA_PICTURE_POSITION_Y, Config.DATA_DEFAULT_PICTURE_POSITION_Y);
        ImageView imageView = WindowsMethods.createPictureView(mContext, bitmap, zoom);
        ImageMethods.saveImageViewById(mContext, id, imageView);
        if (pictureData.getBoolean(Config.DATA_PICTURE_SHOW_ENABLED, Config.DATA_DEFAULT_PICTURE_SHOW_ENABLED)) {
            WindowsMethods.createWindow(windowManager, imageView, position_x, position_y);
        }
    }

    public static void DeleteWin(Context mContext, String id) {
        PictureData pictureData = new PictureData();
        pictureData.setDataControl(id);
        if(pictureData.getBoolean(Config.DATA_PICTURE_SHOW_ENABLED, Config.DATA_DEFAULT_PICTURE_SHOW_ENABLED)) {
            ImageView imageView = ImageMethods.getImageViewById(mContext, id);
            if (imageView != null) {
                WindowsMethods.getWindowManager(mContext).removeView(imageView);
            }
        }
        pictureData.remove();
        ImageMethods.clearAllTemp(mContext, id);
    }

}
