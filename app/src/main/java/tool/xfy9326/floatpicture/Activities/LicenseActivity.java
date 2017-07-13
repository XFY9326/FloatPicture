package tool.xfy9326.floatpicture.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import tool.xfy9326.floatpicture.Methods.IOMethods;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;

public class LicenseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ViewSet();
    }

    private void ViewSet() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout main_layout = (LinearLayout) findViewById(R.id.layout_license);
        String license_url_application = getString(R.string.website);
        String license_path_application = Config.LICENSE_PATH_APPLICATION;
        String license_application = IOMethods.readAssetText(this, license_path_application);

        addLicense(getString(R.string.app_name), license_url_application, license_application, main_layout);
    }

    private void addLicense(String title, String html, String data, LinearLayout main_layout) {
        LayoutInflater inflater = LayoutInflater.from(LicenseActivity.this);
        View layout = inflater.inflate(R.layout.widget_card_license, (ViewGroup) findViewById(R.id.layout_license_card));
        TextView textview_title = (TextView) layout.findViewById(R.id.licence_title);
        textview_title.setText(title);
        TextView textview_data = (TextView) layout.findViewById(R.id.licence_data);
        textview_data.setText(data);
        TextView textview_url = (TextView) layout.findViewById(R.id.licence_url);
        textview_url.setText(html);
        textview_url.setMovementMethod(LinkMovementMethod.getInstance());
        main_layout.addView(layout);
    }
}
