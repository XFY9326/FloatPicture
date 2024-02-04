package tool.xfy9326.floatpicture.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Objects;

import tool.xfy9326.floatpicture.Activities.MainActivity;
import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.Methods.ManageMethods;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.View.ManageListAdapter;

public class NotificationService extends Service {
    private static final String CHANNEL_ID = "channel_default";
    private RemoteViews remoteViews;
    private NotificationCompat.Builder builder_manage;
    private NotificationButtonBroadcastReceiver notificationButtonBroadcastReceiver;

    private static void createNotificationChannel(@NonNull Context context, @NonNull NotificationManagerCompat notificationManager) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_LOW);
                notificationChannel.setDescription(context.getString(R.string.notification_channel_des));
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationChannel.setShowBadge(false);
                notificationChannel.enableLights(false);
                notificationChannel.enableVibration(false);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    @SuppressLint({"ForegroundServiceType", "UnspecifiedRegisterReceiverFlag"})
    @Override
    public void onCreate() {
        super.onCreate();
        if (notificationButtonBroadcastReceiver == null) {
            notificationButtonBroadcastReceiver = new NotificationButtonBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Config.INTENT_ACTION_NOTIFICATION_BUTTON_CLICK);
            intentFilter.addAction(Config.INTENT_ACTION_NOTIFICATION_UPDATE_COUNT);
            registerReceiver(notificationButtonBroadcastReceiver, intentFilter);
        }
        if (builder_manage == null) {
            builder_manage = createNotification();
            startForeground(Config.NOTIFICATION_ID, builder_manage.build());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationButtonBroadcastReceiver != null) {
            unregisterReceiver(notificationButtonBroadcastReceiver);
            notificationButtonBroadcastReceiver = null;
        }
        if (builder_manage != null) {
            stopForeground(true);
            builder_manage = null;
        }
    }

    private NotificationCompat.Builder createNotification() {
        MainApplication mainApplication = (MainApplication) getApplicationContext();
        mainApplication.setWinVisible(true);
        //Create Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel(this, notificationManager);

        builder.setSmallIcon(R.drawable.ic_notification);

        //Content Intent
        Intent intent_main = new Intent(this, MainActivity.class);
        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent_main = PendingIntent.getActivity(this, 0, intent_main, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent_main);

        //Content View
        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_manage);
        remoteViews.setImageViewResource(R.id.imageview_notification_application, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.textview_picture_num, getString(R.string.notification_picture_count, String.valueOf(mainApplication.getViewCount())));

        remoteViews.setImageViewResource(R.id.imageview_set_picture_view, R.drawable.ic_invisible);
        Intent intent_picture_show = new Intent();
        intent_picture_show.setAction(Config.INTENT_ACTION_NOTIFICATION_BUTTON_CLICK);
        PendingIntent pendingIntent_picture_show = PendingIntent.getBroadcast(this, 1, intent_picture_show, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imageview_set_picture_view, pendingIntent_picture_show);

        builder.setContent(remoteViews);
        return builder;
    }

    private class NotificationButtonBroadcastReceiver extends BroadcastReceiver {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (remoteViews != null) {
                MainApplication mainApplication = (MainApplication) getApplicationContext();
                if (Objects.equals(intent.getAction(), Config.INTENT_ACTION_NOTIFICATION_BUTTON_CLICK)) {
                    if (mainApplication.getWinVisible()) {
                        ManageMethods.setAllWindowsVisible(context, false);
                        remoteViews.setImageViewResource(R.id.imageview_set_picture_view, R.drawable.ic_visible);
                        mainApplication.setWinVisible(false);
                    } else {
                        ManageMethods.setAllWindowsVisible(context, true);
                        remoteViews.setImageViewResource(R.id.imageview_set_picture_view, R.drawable.ic_invisible);
                        mainApplication.setWinVisible(true);
                    }
                    ManageListAdapter manageListAdapter = ((MainApplication) getApplicationContext()).getManageListAdapter();
                    if (manageListAdapter != null) {
                        manageListAdapter.notifyDataSetChanged();
                    }
                } else if (Objects.equals(intent.getAction(), Config.INTENT_ACTION_NOTIFICATION_UPDATE_COUNT)) {
                    remoteViews.setTextViewText(R.id.textview_picture_num, getString(R.string.notification_picture_count, String.valueOf(mainApplication.getViewCount())));
                }
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                builder_manage.setContent(remoteViews);
                Objects.requireNonNull(notificationManager).notify(Config.NOTIFICATION_ID, builder_manage.build());
            }
        }
    }

}
