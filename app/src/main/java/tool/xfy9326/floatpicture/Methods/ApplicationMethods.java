package tool.xfy9326.floatpicture.Methods;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.HashMap;

import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Services.NotificationService;
import tool.xfy9326.floatpicture.Utils.Config;

public class ApplicationMethods {
    private static boolean waitDoubleClick;

    public static void startNotificationControl(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Config.PREFERENCE_SHOW_NOTIFICATION_CONTROL, true)) {
            context.startService(new Intent(context, NotificationService.class));
        }
    }

    private static void closeNotificationControl(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Config.PREFERENCE_SHOW_NOTIFICATION_CONTROL, true)) {
            context.stopService(new Intent(context, NotificationService.class));
        }
    }

    public static String getApplicationVersion(Context mContext) {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionName + " (" + packageInfo.versionCode + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void CloseApplication(Activity mActivity) {
        ManageMethods.CloseAllWindows(mActivity);
        closeNotificationControl(mActivity);
        mActivity.finish();
        System.gc();
    }

    public static void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            navigationView.setVerticalScrollBarEnabled(false);
        }
    }

    public static void DoubleClickCloseSnackBar(final Activity mActivity, boolean isDoubleClick) {
        if (isDoubleClick && waitDoubleClick) {
            CloseApplication(mActivity);
        } else {
            CoordinatorLayout coordinatorLayout = mActivity.findViewById(R.id.main_layout_content);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.action_warn_double_click_close_application, Snackbar.LENGTH_SHORT);
            snackbar.setAction(R.string.action_back_to_launcher, v -> mActivity.moveTaskToBack(true));
            snackbar.setActionTextColor(Color.RED);
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    waitDoubleClick = false;
                    super.onDismissed(transientBottomBar, event);
                }
            });
            waitDoubleClick = true;
            snackbar.show();
        }
    }

    public static void ClearUselessTemp(final Context mContext) {
        new Thread(() -> {
            File dir = new File(Config.DEFAULT_PICTURE_DIR);
            String[] dirList = dir.list();
            if (dir.exists() && dirList != null) {
                if (dirList.length > 0) {
                    HashMap<String, View> hashMap = ((MainApplication) mContext.getApplicationContext()).getRegister();
                    if (!hashMap.isEmpty()) {
                        File[] pictures = dir.listFiles();
                        if (pictures != null) {
                            for (File pic_file : pictures) {
                                if (!hashMap.containsKey(pic_file.getName())) {
                                    //noinspection ResultOfMethodCallIgnored
                                    pic_file.delete();
                                    File temp_file = new File(Config.DEFAULT_PICTURE_TEMP_DIR + pic_file.getName());
                                    if (temp_file.exists()) {
                                        //noinspection ResultOfMethodCallIgnored
                                        temp_file.delete();
                                    }
                                }
                            }
                        }
                    } else {
                        //noinspection ResultOfMethodCallIgnored
                        dir.delete();
                    }
                }
            }
        }).start();
    }
}

