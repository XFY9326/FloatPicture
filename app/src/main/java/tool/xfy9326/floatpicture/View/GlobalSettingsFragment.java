package tool.xfy9326.floatpicture.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Objects;

import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;

public class GlobalSettingsFragment extends PreferenceFragmentCompat {
    private LayoutInflater inflater;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(requireActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_global_settings);
        PreferenceSet();
    }

    @NonNull
    private Preference requirePreference(CharSequence key) {
        return Objects.requireNonNull(findPreference(key));
    }

    private void PreferenceSet() {
        requirePreference(Config.PREFERENCE_NEW_PICTURE_QUALITY).setOnPreferenceClickListener(preference -> {
            PictureQualitySet();
            return true;
        });
        requirePreference(Config.PREFERENCE_SHOW_NOTIFICATION_CONTROL).setOnPreferenceChangeListener((preference, newValue) -> {
            Toast.makeText(getActivity(), R.string.restart_to_apply_changes, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void PictureQualitySet() {
        int picture_size = sharedPreferences.getInt(Config.PREFERENCE_NEW_PICTURE_QUALITY, 80);
        View mView = inflater.inflate(R.layout.dialog_set_size, requireActivity().findViewById(R.id.layout_dialog_set_size));
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        dialog.setTitle(R.string.settings_global_picture_quality);
        TextView name = mView.findViewById(R.id.textview_set_size);
        name.setText(R.string.settings_global_picture_quality_quality);
        final SeekBar seekBar = mView.findViewById(R.id.seekbar_set_size);
        seekBar.setProgress(picture_size);
        seekBar.setMax(100);
        final EditText editText = mView.findViewById(R.id.edittext_set_size);
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
        editText.setOnEditorActionListener((v, actionId, event) -> {
            int edittext_temp = Integer.parseInt(v.getText().toString());
            if (edittext_temp > 0) {
                seekBar.setProgress(edittext_temp);
            } else {
                Toast.makeText(getActivity(), R.string.settings_global_picture_quality_warn, Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        dialog.setView(mView);
        dialog.setPositiveButton(R.string.done, (__, which) -> {
            int quality = seekBar.getProgress();
            if (quality > 0) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Config.PREFERENCE_NEW_PICTURE_QUALITY, quality);
                editor.apply();
            } else {
                Toast.makeText(getActivity(), R.string.settings_global_picture_quality_warn, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.show();
    }
}
