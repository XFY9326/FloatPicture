package tool.xfy9326.floatpicture.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.View.GlobalSettingsFragment;


public class GlobalSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ViewSet();
        fragmentSet(savedInstanceState);
    }

    private void ViewSet() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void fragmentSet(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            GlobalSettingsFragment mGlobalSettingsFragment = new GlobalSettingsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_picture_settings_content, mGlobalSettingsFragment);
            fragmentTransaction.commit();
        }
    }

}
