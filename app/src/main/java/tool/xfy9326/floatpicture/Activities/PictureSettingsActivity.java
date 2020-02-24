package tool.xfy9326.floatpicture.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.View.PictureSettingsFragment;

public class PictureSettingsActivity extends AppCompatActivity {
    private PictureSettingsFragment mPictureSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ViewSet();
        fragmentSet(savedInstanceState);
        setBackResult();
    }

    private void ViewSet() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        if (actionBar != null && intent != null) {
            if (!intent.getBooleanExtra(Config.INTENT_PICTURE_EDIT_MODE, false)) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void fragmentSet(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mPictureSettingsFragment = new PictureSettingsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_picture_settings_content, mPictureSettingsFragment);
            fragmentTransaction.commit();
        } else {
            mPictureSettingsFragment = (PictureSettingsFragment) getSupportFragmentManager().findFragmentById(R.id.layout_picture_settings_content);
        }
    }

    private void setBackResult() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra(Config.INTENT_PICTURE_EDIT_MODE, false)) {
                Intent result_intent = new Intent();
                result_intent.putExtra(Config.INTENT_PICTURE_EDIT_POSITION, getIntent().getIntExtra(Config.INTENT_PICTURE_EDIT_POSITION, -1));
                setResult(Config.REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_CHANGE, result_intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        mPictureSettingsFragment.exit();
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_picture_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_picture_settings_save:
                mPictureSettingsFragment.saveAllData();
                finish();
                break;
            case android.R.id.home:
                mPictureSettingsFragment.exit();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mPictureSettingsFragment.clearEditView();
        System.gc();
        super.onDestroy();
    }
}
