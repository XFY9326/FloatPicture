package tool.xfy9326.floatpicture.View;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;

public class GlobalSettingsFragment extends PreferenceFragment {
    private LayoutInflater inflater;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_global_settings);
        inflater = LayoutInflater.from(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        PreferenceSet();
    }

    private void PreferenceSet() {
        Preference new_picture_quality = findPreference(Config.PREFERENCE_NEW_PICTURE_QUALITY);
        new_picture_quality.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                PictureQualitySet();
                return true;
            }
        });
    }

    private void PictureQualitySet() {
        int picture_size = sharedPreferences.getInt(Config.PREFERENCE_NEW_PICTURE_QUALITY, 80);
        View mView = inflater.inflate(R.layout.dialog_set_size, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_set_size));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.settings_global_picture_quality);
        TextView name = (TextView) mView.findViewById(R.id.textview_set_size);
        name.setText(R.string.settings_global_picture_quality_quality);
        final SeekBar seekBar = (SeekBar) mView.findViewById(R.id.seekbar_set_size);
        seekBar.setProgress(picture_size);
        seekBar.setMax(100);
        final EditText editText = (EditText) mView.findViewById(R.id.edittext_set_size);
        editText.setText(String.valueOf(picture_size));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int edittext_temp = Integer.valueOf(v.getText().toString());
                if (edittext_temp > 0) {
                    seekBar.setProgress(edittext_temp);
                } else {
                    Toast.makeText(getActivity(), R.string.settings_global_picture_quality_warn, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        dialog.setView(mView);
        dialog.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int quality = seekBar.getProgress();
                if (quality > 0) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Config.PREFERENCE_NEW_PICTURE_QUALITY, quality);
                    editor.apply();
                } else {
                    Toast.makeText(getActivity(), R.string.settings_global_picture_quality_warn, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.show();
    }
}
