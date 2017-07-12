package tool.xfy9326.floatpicture.Methods;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.File;
import java.util.HashMap;

import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;

public class ApplicationMethods {
    private static boolean waitDoubleClick;

    public static void CloseApplication(Activity mActivity) {
        ManageMethods.CloseAllWindows(mActivity);
        mActivity.finish();
        System.gc();
    }

    public static void DoubleClickCloseSnackBar(final Activity mActivity, boolean isDoubleClick) {
        if (isDoubleClick && waitDoubleClick) {
            CloseApplication(mActivity);
        } else {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) mActivity.findViewById(R.id.main_layout_content);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.action_warn_double_click_close_application, Snackbar.LENGTH_SHORT);
            snackbar.setAction(R.string.action_back_to_launcher, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.moveTaskToBack(true);
                }
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
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

    public static void ClearUselessTemp(Context mContext) {
        File dir = new File(Config.DEFAULT_PICTURE_DIR);
        if (dir.list().length > 0) {
            HashMap<String, View> hashMap = ((MainApplication) mContext.getApplicationContext()).getRegister();
            if (hashMap.size() > 0) {
                File[] pictures = dir.listFiles();
                for (File temp_file : pictures) {
                    if (!hashMap.containsKey(temp_file.getName())) {
                        //noinspection ResultOfMethodCallIgnored
                        temp_file.delete();
                    }
                }
            } else {
                //noinspection ResultOfMethodCallIgnored
                dir.delete();
            }
        }
    }
}

