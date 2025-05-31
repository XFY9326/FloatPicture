package tool.xfy9326.floatpicture.Methods;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import tool.xfy9326.floatpicture.R;

public class PermissionMethods {

    public static final String[] StoragePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void askPermission(Activity mActivity, String[] permissions, int requestCode) {
        if (!checkPermission(mActivity, permissions)) {
            ActivityCompat.requestPermissions(mActivity, permissions, requestCode);
        }
    }

    static boolean checkPermission(Context mContext, String[] permissions) {
        boolean result = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
                break;
            }
        }
        return result;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressWarnings("SameParameterValue")
    public static void askOverlayPermission(final Activity mActivity, final int requestCode) {
        if (!Settings.canDrawOverlays(mActivity)) {
            AlertDialog.Builder overlayPermission = new AlertDialog.Builder(mActivity);
            overlayPermission.setTitle(R.string.permission_warn);
            overlayPermission.setMessage(R.string.permission_warn_overlay_explanation);
            overlayPermission.setPositiveButton(R.string.done, (dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                mActivity.startActivityForResult(intent, requestCode);
            });
            overlayPermission.setNegativeButton(R.string.cancel, (dialogInterface, i) -> mActivity.finish());
            overlayPermission.setCancelable(false);
            overlayPermission.show();
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public static void delayOverlayPermissionCheck(final Context mContext) {
        if (!Settings.canDrawOverlays(mContext)) {
            new Handler().postDelayed(() -> {
                if (!Settings.canDrawOverlays(mContext)) {
                    Toast.makeText(mContext, R.string.permission_warn_overlay_intent, Toast.LENGTH_SHORT).show();
                } else {
                    if (PermissionMethods.checkPermission(mContext, PermissionMethods.StoragePermission)) {
                        ManageMethods.RunWin(mContext);
                    }
                }
            }, 2000);
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static void explainPermission(final Activity mActivity, final String[] permissions, final int requestCode) {
        AlertDialog.Builder permissionExplanation = new AlertDialog.Builder(mActivity);
        permissionExplanation.setTitle(R.string.permission_warn);
        permissionExplanation.setMessage(R.string.permission_warn_explanation);
        permissionExplanation.setPositiveButton(R.string.done, (dialogInterface, i) -> askPermission(mActivity, permissions, requestCode));
        permissionExplanation.setNegativeButton(R.string.done, (dialogInterface, i) -> mActivity.finish());
        permissionExplanation.setCancelable(false);
        permissionExplanation.show();
    }
}
