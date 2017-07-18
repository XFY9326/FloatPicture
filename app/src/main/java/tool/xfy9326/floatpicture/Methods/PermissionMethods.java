package tool.xfy9326.floatpicture.Methods;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

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

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressWarnings("SameParameterValue")
    public static void askOverlayPermission(final Activity mActivity, final int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mActivity)) {
                AlertDialog.Builder overlayPermission = new AlertDialog.Builder(mActivity);
                overlayPermission.setTitle(R.string.permission_warn);
                overlayPermission.setMessage(R.string.permission_warn_overlay_explanation);
                overlayPermission.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                        mActivity.startActivityForResult(intent, requestCode);
                    }
                });
                overlayPermission.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mActivity.finish();
                    }
                });
                overlayPermission.setCancelable(false);
                overlayPermission.show();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void delayOverlayPermissionCheck(final Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mContext)) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (!Settings.canDrawOverlays(mContext)) {
                            Toast.makeText(mContext, R.string.permission_warn_overlay_intent, Toast.LENGTH_SHORT).show();
                        } else {
                            if (PermissionMethods.checkPermission(mContext, PermissionMethods.StoragePermission)) {
                                ManageMethods.RunWin(mContext);
                            }
                        }
                    }
                }, 2000);
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static void explainPermission(final Activity mActivity, final String[] permissions, final int requestCode) {
        AlertDialog.Builder permissionExplanation = new AlertDialog.Builder(mActivity);
        permissionExplanation.setTitle(R.string.permission_warn);
        permissionExplanation.setMessage(R.string.permission_warn_explanation);
        permissionExplanation.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                askPermission(mActivity, permissions, requestCode);
            }
        });
        permissionExplanation.setNegativeButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mActivity.finish();
            }
        });
        permissionExplanation.setCancelable(false);
        permissionExplanation.show();
    }
}
