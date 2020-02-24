package tool.xfy9326.floatpicture.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import tool.xfy9326.floatpicture.Methods.IOMethods;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;

public class LicenseActivity extends AppCompatActivity {
    private LayoutInflater inflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        inflater = LayoutInflater.from(LicenseActivity.this);
        ViewSet();
    }

    private void ViewSet() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout main_layout = findViewById(R.id.layout_license);

        String license_application = IOMethods.readAssetText(this, Config.LICENSE_PATH_APPLICATION);

        addLicense(getString(R.string.app_name), getString(R.string.website), license_application, main_layout);
    }

    private void addLicense(String title, String url, String data, LinearLayout main_layout) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.widget_card_license, (ViewGroup) findViewById(R.id.layout_license_card));
        TextView textview_title = layout.findViewById(R.id.licence_title);
        textview_title.setText(title);
        TextView textview_url = layout.findViewById(R.id.licence_url);
        textview_url.setText(url);
        TextView textview_data = layout.findViewById(R.id.licence_data);
        textview_data.setText(data);
        main_layout.addView(layout);
    }
}
