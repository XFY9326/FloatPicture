package tool.xfy9326.floatpicture.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.Methods.ManageMethods;
import tool.xfy9326.floatpicture.Utils.Config;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Config.PREFERENCE_BOOT_AUTO_RUN, false)) {
                MainApplication mainApplication = (MainApplication) context.getApplicationContext();
                if (!mainApplication.isApplicationInit()) {
                    ManageMethods.RunWin(context);
                    mainApplication.setApplicationInit(true);
                }
            }
        }
    }
}
