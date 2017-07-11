package tool.xfy9326.floatpicture.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import tool.xfy9326.floatpicture.Methods.ManageMethods;
import tool.xfy9326.floatpicture.Methods.PermissionMethods;
import tool.xfy9326.floatpicture.Methods.UriMethods;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.View.ManageListAdapter;

public class MainActivity extends AppCompatActivity {
    private final static String APPLICATION_STARTED = "APPLICATION_STARTED";
    private ManageListAdapter manageListAdapter;

    public static void SnackShow(Activity mActivity, int resourceId) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) mActivity.findViewById(R.id.main_layout_content);
        Snackbar.make(coordinatorLayout, mActivity.getString(resourceId), Snackbar.LENGTH_SHORT).show();
        System.gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionMethods.askOverlayPermission(this, Config.REQUEST_CODE_PERMISSION_OVERLAY);
        PermissionMethods.askPermission(this, PermissionMethods.StoragePermission, Config.REQUEST_CODE_PERMISSION_STORAGE);
        ViewSet();
        if (savedInstanceState == null) {
            ManageMethods.RunWin(this);
        }
    }

    private void ViewSet() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manageListAdapter = new ManageListAdapter(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_list_manage);
        recyclerView.setAdapter(manageListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.main_button_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManageMethods.SelectPicture(MainActivity.this);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean(APPLICATION_STARTED, true);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_ADD) {
            manageListAdapter.updateData();
            if (manageListAdapter.getItemCount() != 0) {
                if (manageListAdapter.getItemCount() == 1) {
                    manageListAdapter.notifyDataSetChanged();
                } else {
                    manageListAdapter.notifyItemInserted(manageListAdapter.getItemCount());
                }
                SnackShow(this, R.string.action_add_window);
            }
        } else if (requestCode == Config.REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_CHANGE) {
            if (data != null) {
                int position = data.getIntExtra(Config.INTENT_PICTURE_EDIT_POSITION, -1);
                if (position >= 0) {
                    manageListAdapter.updateData();
                    manageListAdapter.notifyItemChanged(position);
                }
            }
        } else if (requestCode == Config.REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_GET_PICTURE) {
            if (data != null) {
                Intent intent = new Intent(MainActivity.this, PictureSettingsActivity.class);
                intent.putExtra(Config.INTENT_PICTURE_EDIT_MODE, false);
                intent.putExtra(Config.INTENT_PICTURE_CHOOSE_PICTURE, UriMethods.getImageAbsolutePath(this, data.getData()));
                startActivityForResult(intent, Config.REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_ADD);
            }
        } else if (requestCode == Config.REQUEST_CODE_PERMISSION_OVERLAY) {
            PermissionMethods.delayOverlayPermissionCheck(this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Config.REQUEST_CODE_PERMISSION_STORAGE) {
            boolean run = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        PermissionMethods.explainPermission(this, PermissionMethods.StoragePermission, Config.REQUEST_CODE_PERMISSION_STORAGE);
                    } else {
                        Toast.makeText(this, R.string.permission_warn_storage_intent, Toast.LENGTH_SHORT).show();
                    }
                    run = false;
                    break;
                }
            }
            if (run && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    ManageMethods.RunWin(this);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
