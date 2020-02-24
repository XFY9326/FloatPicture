package tool.xfy9326.floatpicture.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import tool.xfy9326.floatpicture.MainApplication;
import tool.xfy9326.floatpicture.Methods.ApplicationMethods;
import tool.xfy9326.floatpicture.Methods.IOMethods;
import tool.xfy9326.floatpicture.Methods.ManageMethods;
import tool.xfy9326.floatpicture.Methods.PermissionMethods;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.View.AdvancedRecyclerView;
import tool.xfy9326.floatpicture.View.ManageListAdapter;

public class MainActivity extends AppCompatActivity {
    private ManageListAdapter manageListAdapter;
    private long BackClickTime;

    public static void SnackShow(Activity mActivity, int resourceId) {
        CoordinatorLayout coordinatorLayout = mActivity.findViewById(R.id.main_layout_content);
        Snackbar.make(coordinatorLayout, mActivity.getString(resourceId), Snackbar.LENGTH_SHORT).show();
        System.gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
        ApplicationMethods.startNotificationControl(this);
        ApplicationMethods.ClearUselessTemp(this);
    }

    private void init(Bundle savedInstanceState) {
        BackClickTime = System.currentTimeMillis();
        PermissionMethods.askOverlayPermission(this, Config.REQUEST_CODE_PERMISSION_OVERLAY);
        PermissionMethods.askPermission(this, PermissionMethods.StoragePermission, Config.REQUEST_CODE_PERMISSION_STORAGE);
        ViewSet();
        MainApplication mainApplication = (MainApplication) getApplicationContext();
        if (mainApplication.isAppInit() || savedInstanceState == null) {
            ManageMethods.RunWin(this);
            mainApplication.setAppInit(true);
            IOMethods.setNoMedia();
        }
    }

    private void ViewSet() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manageListAdapter = new ManageListAdapter(this);
        ((MainApplication) getApplicationContext()).setManageListAdapter(manageListAdapter);
        AdvancedRecyclerView recyclerView = findViewById(R.id.main_list_manage);
        recyclerView.setAdapter(manageListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setEmptyView(findViewById(R.id.layout_widget_empty_view));

        FloatingActionButton floatingActionButton = findViewById(R.id.main_button_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManageMethods.SelectPicture(MainActivity.this);
            }
        });

        final DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.main_navigation_view);
        ApplicationMethods.disableNavigationViewScrollbars(navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.menu_global_settings:
                        startActivity(new Intent(MainActivity.this, GlobalSettingsActivity.class));
                        break;
                    case R.id.menu_about:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                    case R.id.menu_back_to_launcher:
                        MainActivity.this.moveTaskToBack(true);
                        break;
                    case R.id.menu_exit:
                        ApplicationMethods.CloseApplication(MainActivity.this);
                        break;
                }
                return false;
            }
        });
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
                ManageMethods.updateNotificationCount(this);
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
                intent.setData(data.getData());
                startActivityForResult(intent, Config.REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_ADD);
            }
        } else if (requestCode == Config.REQUEST_CODE_PERMISSION_OVERLAY) {
            PermissionMethods.delayOverlayPermissionCheck(this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        long BackNowClickTime = System.currentTimeMillis();
        if ((BackNowClickTime - BackClickTime) < 2200) {
            MainApplication mainApplication = (MainApplication) getApplicationContext();
            mainApplication.setAppInit(false);
            ApplicationMethods.DoubleClickCloseSnackBar(this, true);
        } else {
            ApplicationMethods.DoubleClickCloseSnackBar(this, false);
            BackClickTime = System.currentTimeMillis();
        }
    }
}
