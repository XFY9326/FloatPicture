package tool.xfy9326.floatpicture.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import tool.xfy9326.floatpicture.Methods.ImageMethods;
import tool.xfy9326.floatpicture.Methods.WindowsMethods;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.Utils.DataSerializable;
import tool.xfy9326.floatpicture.Utils.PictureData;

public class PictureSettingsFragment extends PreferenceFragment {
    private final static String WINDOW_CREATED = "WINDOW_CREATED";
    private final static String WINDOW_PICTURE_DATA = "WINDOW_PICTURE_DATA";
    private boolean Edit_Mode;
    private boolean Window_Created;
    private LayoutInflater inflater;
    private PictureData pictureData;
    private String PictureId;
    private String PictureName;
    private WindowManager windowManager;
    private ImageView imageView;
    private Bitmap bitmap;
    private float zoom;
    private float zoom_temp;
    private int position_x;
    private int position_y;
    private int position_x_temp;
    private int position_y_temp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        addPreferencesFromResource(R.xml.fragment_picture_settings);
        Window_Created = false;
        Edit_Mode = false;
        pictureData = new PictureData();
        inflater = LayoutInflater.from(getActivity());
        windowManager = WindowsMethods.getWindowManager(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        restoreData(savedInstanceState);
        setMode();
        PreferenceSet();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(WINDOW_CREATED, true);
        DataSerializable dataSerializable = new DataSerializable();
        dataSerializable.setImageView(imageView);
        dataSerializable.setPictureId(PictureId);
        dataSerializable.setPictureName(PictureName);
        dataSerializable.setPosition_X(position_x);
        dataSerializable.setPosition_Y(position_y);
        dataSerializable.setZoom(zoom);
        outState.putSerializable(WINDOW_PICTURE_DATA, dataSerializable);
        super.onSaveInstanceState(outState);
    }

    private void restoreData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Window_Created = savedInstanceState.getBoolean(WINDOW_CREATED, false);
            if (Window_Created) {
                DataSerializable dataSerializable = (DataSerializable) savedInstanceState.getSerializable(WINDOW_PICTURE_DATA);
                if (dataSerializable != null) {
                    imageView = dataSerializable.getImageView();
                    PictureId = dataSerializable.getPictureId();
                    pictureData.setDataControl(PictureId);
                    PictureName = dataSerializable.getPictureName();
                    position_x = dataSerializable.getPosition_X();
                    position_y = dataSerializable.getPosition_Y();
                    zoom = dataSerializable.getZoom();
                }
                bitmap = ImageMethods.getPictureById(PictureId);
                windowManager = WindowsMethods.getWindowManager(getActivity());
            }
        }
    }

    private void setMode() {
        Intent intent = getActivity().getIntent();
        Edit_Mode = intent.getBooleanExtra(Config.INTENT_PICTURE_EDIT_MODE, false);
        if (!Window_Created) {
            if (Edit_Mode) {
                PictureId = intent.getStringExtra(Config.INTENT_PICTURE_EDIT_ID);
                pictureData.setDataControl(PictureId);
                PictureName = pictureData.getListArray().get(PictureId);
                position_x = pictureData.getInt(Config.DATA_PICTURE_POSITION_X, Config.DATA_DEFAULT_PICTURE_POSITION_X);
                position_y = pictureData.getInt(Config.DATA_PICTURE_POSITION_Y, Config.DATA_DEFAULT_PICTURE_POSITION_Y);
                bitmap = ImageMethods.getPictureById(PictureId);
                zoom = pictureData.getFloat(Config.DATA_PICTURE_ZOOM, ImageMethods.getDefaultZoom(getActivity(), bitmap, false));
                imageView = ImageMethods.getImageViewById(getActivity(), PictureId);
            } else {
                PictureId = ImageMethods.setNewImage(getActivity(), new File(intent.getStringExtra(Config.INTENT_PICTURE_CHOOSE_PICTURE)));
                pictureData.setDataControl(PictureId);
                PictureName = getString(R.string.new_picture_name);
                position_x = Config.DATA_DEFAULT_PICTURE_POSITION_X;
                position_y = Config.DATA_DEFAULT_PICTURE_POSITION_Y;
                bitmap = ImageMethods.getPictureById(PictureId);
                zoom = ImageMethods.getDefaultZoom(getActivity(), bitmap, false);
                imageView = ImageMethods.createPictureView(getActivity(), bitmap, zoom);
                WindowsMethods.createWindow(windowManager, imageView, position_x, position_y);
            }
        }
    }

    private void PreferenceSet() {
        Preference picture_name = findPreference(Config.PREFERENCE_PICTURE_NAME);
        picture_name.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setPictureName();
                return false;
            }
        });
        Preference picture_id = findPreference(Config.PREFERENCE_PICTURE_ID);
        picture_id.setSummary("ID:" + PictureId);
        Preference picture_size = findPreference(Config.PREFERENCE_PICTURE_RESIZE);
        picture_size.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setPictureSize();
                return false;
            }
        });
        Preference picture_position = findPreference(Config.PREFERENCE_PICTURE_POSITION);
        picture_position.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setPicturePosition();
                return false;
            }
        });
    }

    private void setPictureName() {
        View mView = inflater.inflate(R.layout.dialog_edit_text, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_edit_text));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.settings_picture_name);
        final EditText editText = (EditText) mView.findViewById(R.id.edittext_dialog);
        editText.setText(PictureName);
        dialog.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.settings_picture_name_warn, Toast.LENGTH_SHORT).show();
                } else {
                    PictureName = editText.getText().toString();
                }
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.settings_picture_name_warn, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setView(mView);
        dialog.show();
    }

    private void setPictureSize() {
        final Bitmap bitmap_Edit = ImageMethods.getEditBitmap(getActivity(), bitmap);
        final ImageView imageView_Edit = ImageMethods.createPictureView(getActivity(), bitmap_Edit, zoom);
        onEditPicture(imageView_Edit);

        View mView = inflater.inflate(R.layout.dialog_set_size, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_set_size));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.settings_picture_resize);
        dialog.setCancelable(false);
        final float Max_Size = ImageMethods.getDefaultZoom(getActivity(), bitmap, true) * 100;
        TextView name = (TextView) mView.findViewById(R.id.textview_set_size);
        name.setText(R.string.settings_picture_resize_size);
        final SeekBar seekBar = (SeekBar) mView.findViewById(R.id.seekbar_set_size);
        seekBar.setProgress((int) (zoom * 100));
        seekBar.setMax((int) Max_Size);
        final EditText editText = (EditText) mView.findViewById(R.id.edittext_set_size);
        editText.setText(String.valueOf(zoom));
        zoom_temp = zoom;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0) {
                    zoom_temp = ((float) progress) / 100;
                    editText.setText(String.valueOf(zoom_temp));
                    WindowsMethods.updateWindow(windowManager, imageView_Edit, bitmap_Edit, zoom_temp, position_x, position_y);
                }
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
                float edittext_temp = Float.valueOf(v.getText().toString());
                if (edittext_temp > 0 && edittext_temp <= Max_Size) {
                    zoom_temp = edittext_temp;
                    seekBar.setProgress((int) (zoom_temp * 100));
                    WindowsMethods.updateWindow(windowManager, imageView_Edit, bitmap_Edit, zoom_temp, position_x, position_y);
                } else {
                    Toast.makeText(getActivity(), R.string.settings_picture_resize_warn, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        dialog.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                zoom = zoom_temp;
                onSuccessEditPicture(imageView_Edit, bitmap_Edit);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFailedEditPicture(imageView_Edit, bitmap_Edit);
            }
        });
        dialog.setView(mView);
        dialog.show();
    }

    private void setPicturePosition() {
        final Bitmap bitmap_Edit = ImageMethods.getEditBitmap(getActivity(), bitmap);
        final ImageView imageView_Edit = ImageMethods.createPictureView(getActivity(), bitmap_Edit, zoom);
        onEditPicture(imageView_Edit);

        View mView = inflater.inflate(R.layout.dialog_set_position, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_set_position));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.settings_picture_position);
        dialog.setCancelable(false);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        final int Max_X = size.x;
        final int Max_Y = size.y;
        final SeekBar seekBar_x = (SeekBar) mView.findViewById(R.id.seekbar_set_position_x);
        seekBar_x.setMax(Max_X);
        seekBar_x.setProgress(position_x);
        final EditText editText_x = (EditText) mView.findViewById(R.id.edittext_set_position_x);
        editText_x.setText(String.valueOf(position_x));
        final SeekBar seekBar_y = (SeekBar) mView.findViewById(R.id.seekbar_set_position_y);
        seekBar_y.setMax(Max_Y);
        seekBar_y.setProgress(position_y);
        final EditText editText_y = (EditText) mView.findViewById(R.id.edittext_set_position_y);
        editText_y.setText(String.valueOf(position_y));
        position_x_temp = position_x;
        position_y_temp = position_y;
        seekBar_x.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                position_x_temp = progress;
                editText_x.setText(String.valueOf(progress));
                WindowsMethods.updateWindow(windowManager, imageView_Edit, bitmap_Edit, zoom, position_x_temp, position_y_temp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        editText_x.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int edittext_temp = Integer.valueOf(v.getText().toString());
                if (edittext_temp >= 0 && edittext_temp <= Max_X) {
                    position_x_temp = edittext_temp;
                    seekBar_x.setProgress(edittext_temp);
                    WindowsMethods.updateWindow(windowManager, imageView_Edit, bitmap_Edit, zoom, position_x_temp, position_y_temp);
                } else {
                    Toast.makeText(getActivity(), R.string.settings_picture_position_warn, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        seekBar_y.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                position_y_temp = progress;
                editText_y.setText(String.valueOf(progress));
                WindowsMethods.updateWindow(windowManager, imageView_Edit, bitmap_Edit, zoom, position_x_temp, position_y_temp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        editText_y.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int edittext_temp = Integer.valueOf(v.getText().toString());
                if (edittext_temp >= 0 && edittext_temp <= Max_Y) {
                    position_y_temp = edittext_temp;
                    seekBar_y.setProgress(edittext_temp);
                    WindowsMethods.updateWindow(windowManager, imageView_Edit, bitmap_Edit, zoom, position_x_temp, position_y_temp);
                } else {
                    Toast.makeText(getActivity(), R.string.settings_picture_position_warn, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        dialog.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                position_x = position_x_temp;
                position_y = position_y_temp;
                onSuccessEditPicture(imageView_Edit, bitmap_Edit);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFailedEditPicture(imageView_Edit, bitmap_Edit);
            }
        });
        dialog.setView(mView);
        dialog.show();
    }

    private void onEditPicture(ImageView imageView_Edit) {
        windowManager.removeView(imageView);
        imageView.refreshDrawableState();
        WindowsMethods.createWindow(windowManager, imageView_Edit, position_x, position_y);
    }

    private void onSuccessEditPicture(ImageView imageView_Edit, Bitmap bitmap_Edit) {
        windowManager.removeView(imageView_Edit);
        imageView_Edit.refreshDrawableState();
        bitmap_Edit.recycle();
        imageView.setImageBitmap(ImageMethods.resizeBitmap(bitmap, zoom));
        WindowsMethods.createWindow(windowManager, imageView, position_x, position_y);
    }

    private void onFailedEditPicture(ImageView imageView_Edit, Bitmap bitmap_Edit) {
        windowManager.removeView(imageView_Edit);
        imageView_Edit.refreshDrawableState();
        bitmap_Edit.recycle();
        WindowsMethods.createWindow(windowManager, imageView, position_x, position_y);
    }

    public void saveAllData() {
        pictureData.put(Config.DATA_PICTURE_SHOW_ENABLED, true);
        pictureData.put(Config.DATA_PICTURE_ZOOM, zoom);
        pictureData.put(Config.DATA_PICTURE_POSITION_X, position_x);
        pictureData.put(Config.DATA_PICTURE_POSITION_Y, position_y);
        pictureData.commit(PictureName);
        ImageMethods.saveImageViewById(getActivity(), PictureId, imageView);
        WindowsMethods.updateWindow(windowManager, imageView, bitmap, zoom, position_x, position_y);
    }

    public void exit() {
        if (!Edit_Mode) {
            if (imageView != null) {
                windowManager.removeView(imageView);
                bitmap.recycle();
                imageView = null;
            }
            ImageMethods.clearAllTemp(getActivity(), PictureId);
        } else {
            float original_zoom = pictureData.getFloat(Config.DATA_PICTURE_ZOOM, zoom);
            int original_position_x = pictureData.getInt(Config.DATA_PICTURE_POSITION_X, position_x);
            int original_position_y = pictureData.getInt(Config.DATA_PICTURE_POSITION_Y, position_y);
            WindowsMethods.updateWindow(windowManager, imageView, bitmap, original_zoom, original_position_x, original_position_y);
        }

    }

}
