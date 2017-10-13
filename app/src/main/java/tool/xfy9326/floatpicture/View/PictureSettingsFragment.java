package tool.xfy9326.floatpicture.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import tool.xfy9326.floatpicture.Methods.ImageMethods;
import tool.xfy9326.floatpicture.Methods.WindowsMethods;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.Utils.PictureData;

public class PictureSettingsFragment extends PreferenceFragment {
    private final static String WINDOW_CREATED = "WINDOW_CREATED";
    private boolean Edit_Mode;
    private boolean Window_Created;
    private boolean onUseEditPicture = false;
    private LayoutInflater inflater;
    private PictureData pictureData;
    private String PictureId;
    private String PictureName;
    private WindowManager windowManager;
    private FloatImageView floatImageView;
    private Bitmap bitmap;
    private Bitmap bitmap_Edit;
    private FloatImageView floatImageView_Edit;
    private boolean touch_and_move;
    private float default_zoom;
    private float zoom;
    private float zoom_temp;
    private float picture_degree;
    private float picture_degree_temp;
    private float picture_alpha;
    private float picture_alpha_temp;
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
        super.onSaveInstanceState(outState);
    }

    private void restoreData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Window_Created = savedInstanceState.getBoolean(WINDOW_CREATED, false);
            windowManager = WindowsMethods.getWindowManager(getActivity());
        }
    }

    private void setMode() {
        AlertDialog.Builder loading = new AlertDialog.Builder(getActivity());
        loading.setCancelable(false);
        loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                WindowsMethods.createWindow(windowManager, floatImageView, touch_and_move, position_x, position_y);
            }
        });
        View mView = inflater.inflate(R.layout.dialog_loading, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_loading));
        loading.setView(mView);
        final AlertDialog alertDialog = loading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getActivity().getIntent();
                Edit_Mode = intent.getBooleanExtra(Config.INTENT_PICTURE_EDIT_MODE, false);
                if (!Window_Created) {
                    if (Edit_Mode) {
                        //Edit
                        PictureId = intent.getStringExtra(Config.INTENT_PICTURE_EDIT_ID);
                        pictureData.setDataControl(PictureId);
                        PictureName = pictureData.getListArray().get(PictureId);
                        position_x = pictureData.getInt(Config.DATA_PICTURE_POSITION_X, Config.DATA_DEFAULT_PICTURE_POSITION_X);
                        position_y = pictureData.getInt(Config.DATA_PICTURE_POSITION_Y, Config.DATA_DEFAULT_PICTURE_POSITION_Y);
                        picture_degree = pictureData.getFloat(Config.DATA_PICTURE_DEGREE, Config.DATA_DEFAULT_PICTURE_DEGREE);
                        picture_alpha = pictureData.getFloat(Config.DATA_PICTURE_ALPHA, Config.DATA_DEFAULT_PICTURE_ALPHA);
                        touch_and_move = pictureData.getBoolean(Config.DATA_PICTURE_TOUCH_AND_MOVE, Config.DATA_DEFAULT_PICTURE_TOUCH_AND_MOVE);
                        bitmap = ImageMethods.getShowBitmap(getActivity(), PictureId);
                        default_zoom = ImageMethods.getDefaultZoom(getActivity(), bitmap, false);
                        zoom = pictureData.getFloat(Config.DATA_PICTURE_ZOOM, default_zoom);
                        floatImageView = ImageMethods.getFloatImageViewById(getActivity(), PictureId);
                    } else {
                        //New
                        PictureId = ImageMethods.setNewImage(getActivity(), new File(intent.getStringExtra(Config.INTENT_PICTURE_CHOOSE_PICTURE)));
                        pictureData.setDataControl(PictureId);
                        PictureName = getString(R.string.new_picture_name);
                        position_x = Config.DATA_DEFAULT_PICTURE_POSITION_X;
                        position_y = Config.DATA_DEFAULT_PICTURE_POSITION_Y;
                        picture_alpha = Config.DATA_DEFAULT_PICTURE_ALPHA;
                        picture_degree = Config.DATA_DEFAULT_PICTURE_DEGREE;
                        touch_and_move = Config.DATA_DEFAULT_PICTURE_TOUCH_AND_MOVE;
                        bitmap = ImageMethods.getShowBitmap(getActivity(), PictureId);
                        default_zoom = ImageMethods.getDefaultZoom(getActivity(), bitmap, false);
                        zoom = default_zoom;
                        floatImageView = ImageMethods.createPictureView(getActivity(), bitmap, touch_and_move, zoom, picture_degree);
                        floatImageView.setAlpha(picture_alpha);
                        floatImageView.setPictureId(PictureId);
                    }
                    alertDialog.cancel();
                }
            }
        }).start();
    }

    private void PreferenceSet() {
        findPreference(Config.PREFERENCE_PICTURE_NAME).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setPictureName();
                return true;
            }
        });
        findPreference(Config.PREFERENCE_PICTURE_RESIZE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setPictureSize();
                return true;
            }
        });
        findPreference(Config.PREFERENCE_PICTURE_DEGREE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setPictureDegree();
                return true;
            }
        });
        findPreference(Config.PREFERENCE_PICTURE_ALPHA).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setPictureAlpha();
                return true;
            }
        });
        CheckBoxPreference preference_touch_and_move = (CheckBoxPreference) findPreference(Config.PREFERENCE_PICTURE_TOUCH_AND_MOVE);
        preference_touch_and_move.setChecked(touch_and_move);
        preference_touch_and_move.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue) {
                    PictureTouchAndMoveAlert();
                    return false;
                } else {
                    setPictureTouchAndMove(false);
                    return true;
                }
            }
        });
        findPreference(Config.PREFERENCE_PICTURE_POSITION).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setPicturePosition();
                return true;
            }
        });
    }

    private void setPictureTouchAndMove(boolean touchable_and_moveable) {
        touch_and_move = touchable_and_moveable;
        windowManager.removeView(floatImageView);
        floatImageView.setMoveable(touchable_and_moveable);
        WindowsMethods.createWindow(windowManager, floatImageView, touchable_and_moveable, position_x, position_y);
    }

    private void PictureTouchAndMoveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.settings_picture_touchable_and_moveable);
        builder.setMessage(R.string.settings_picture_touchable_and_moveable_warn);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((CheckBoxPreference) findPreference(Config.PREFERENCE_PICTURE_TOUCH_AND_MOVE)).setChecked(true);
                setPictureTouchAndMove(true);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
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
        bitmap_Edit = ImageMethods.getEditBitmap(getActivity(), bitmap);
        floatImageView_Edit = ImageMethods.createPictureView(getActivity(), bitmap_Edit, touch_and_move, zoom, picture_degree);
        onEditPicture(floatImageView_Edit);

        View mView = inflater.inflate(R.layout.dialog_set_size, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_set_size));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.settings_picture_resize);
        dialog.setCancelable(false);
        final float Max_Size = ImageMethods.getDefaultZoom(getActivity(), bitmap, true) * 100;
        TextView name = (TextView) mView.findViewById(R.id.textview_set_size);
        name.setText(R.string.settings_picture_resize_size);
        final SeekBar seekBar = (SeekBar) mView.findViewById(R.id.seekbar_set_size);
        seekBar.setMax((int) Max_Size);
        seekBar.setProgress((int) (zoom * 100));
        final EditText editText = (EditText) mView.findViewById(R.id.edittext_set_size);
        editText.setText(String.valueOf(zoom));
        zoom_temp = zoom;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0) {
                    zoom_temp = ((float) progress) / 100;
                    editText.setText(String.valueOf(zoom_temp));
                    WindowsMethods.updateWindow(windowManager, floatImageView_Edit, bitmap_Edit, touch_and_move, zoom_temp, picture_degree, position_x, position_y);
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
                    WindowsMethods.updateWindow(windowManager, floatImageView_Edit, bitmap_Edit, touch_and_move, zoom_temp, picture_degree, position_x, position_y);
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
                onSuccessEditPicture(floatImageView_Edit, bitmap_Edit);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFailedEditPicture(floatImageView_Edit, bitmap_Edit);
            }
        });
        dialog.setView(mView);
        dialog.show();
    }

    private void setPictureDegree() {
        bitmap_Edit = ImageMethods.getEditBitmap(getActivity(), bitmap);
        floatImageView_Edit = ImageMethods.createPictureView(getActivity(), bitmap_Edit, touch_and_move, zoom, picture_degree);
        onEditPicture(floatImageView_Edit);

        View mView = inflater.inflate(R.layout.dialog_set_size, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_set_size));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.settings_picture_degree);
        dialog.setCancelable(false);
        TextView name = (TextView) mView.findViewById(R.id.textview_set_size);
        name.setText(R.string.degree);
        final SeekBar seekBar = (SeekBar) mView.findViewById(R.id.seekbar_set_size);
        seekBar.setMax(3600);
        seekBar.setProgress((int) (picture_degree * 10));
        final EditText editText = (EditText) mView.findViewById(R.id.edittext_set_size);
        editText.setText(String.valueOf(((float) Math.round(picture_degree * 10)) / 10));
        picture_degree_temp = picture_degree;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                picture_degree_temp = ((float) progress) / 10;
                editText.setText(String.valueOf(((float) Math.round(picture_degree_temp * 10)) / 10));
                WindowsMethods.updateWindow(windowManager, floatImageView_Edit, bitmap_Edit, touch_and_move, zoom, picture_degree_temp, position_x, position_y);
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
                if (edittext_temp >= 0 && edittext_temp <= 360) {
                    picture_degree_temp = edittext_temp;
                    seekBar.setProgress((int) (picture_degree_temp * 10));
                    WindowsMethods.updateWindow(windowManager, floatImageView_Edit, bitmap_Edit, touch_and_move, zoom, picture_degree_temp, position_x, position_y);
                } else {
                    Toast.makeText(getActivity(), R.string.settings_number_warn, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        dialog.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                picture_degree = picture_degree_temp;
                onSuccessEditPicture(floatImageView_Edit, bitmap_Edit);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFailedEditPicture(floatImageView_Edit, bitmap_Edit);
            }
        });
        dialog.setView(mView);
        dialog.show();
    }

    private void setPictureAlpha() {
        View mView = inflater.inflate(R.layout.dialog_set_size, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_set_size));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.settings_picture_alpha);
        dialog.setCancelable(false);
        TextView name = (TextView) mView.findViewById(R.id.textview_set_size);
        name.setText(R.string.transparency);
        final SeekBar seekBar = (SeekBar) mView.findViewById(R.id.seekbar_set_size);
        seekBar.setMax(100);
        seekBar.setProgress((int) (picture_alpha * 100));
        final EditText editText = (EditText) mView.findViewById(R.id.edittext_set_size);
        editText.setText(String.valueOf(picture_alpha));
        picture_alpha_temp = picture_alpha;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                picture_alpha_temp = ((float) progress) / 100;
                editText.setText(String.valueOf(picture_alpha_temp));
                floatImageView.setAlpha(picture_alpha_temp);
                WindowsMethods.updateWindow(windowManager, floatImageView, touch_and_move, position_x, position_y);
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
                if (edittext_temp >= 0 && edittext_temp <= 100) {
                    picture_alpha_temp = edittext_temp;
                    seekBar.setProgress((int) (picture_alpha_temp * 100));
                    floatImageView.setAlpha(picture_alpha_temp);
                    WindowsMethods.updateWindow(windowManager, floatImageView, touch_and_move, position_x, position_y);
                } else {
                    Toast.makeText(getActivity(), R.string.settings_number_warn, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        dialog.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                picture_alpha = picture_alpha_temp;
                floatImageView.setAlpha(picture_alpha);
                WindowsMethods.updateWindow(windowManager, floatImageView, touch_and_move, position_x, position_y);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                floatImageView.setAlpha(picture_alpha);
                WindowsMethods.updateWindow(windowManager, floatImageView, touch_and_move, position_x, position_y);
            }
        });
        dialog.setView(mView);
        dialog.show();
    }

    private void setPicturePosition() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final boolean touchable_edit = (touch_and_move || sharedPreferences.getBoolean(Config.PREFERENCE_TOUCHABLE_POSITION_EDIT, false));
        bitmap_Edit = ImageMethods.getEditBitmap(getActivity(), bitmap);
        floatImageView_Edit = ImageMethods.createPictureView(getActivity(), bitmap_Edit, touchable_edit, zoom, picture_degree);
        onEditPicture(floatImageView_Edit);
        if (touchable_edit) {
            WindowsMethods.updateWindow(windowManager, floatImageView_Edit, bitmap_Edit, true, zoom, picture_degree, position_x, position_y);
        }

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
                WindowsMethods.updateWindow(windowManager, floatImageView_Edit, bitmap_Edit, touchable_edit, zoom, picture_degree, position_x_temp, position_y_temp);
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
                    WindowsMethods.updateWindow(windowManager, floatImageView_Edit, bitmap_Edit, touchable_edit, zoom, picture_degree, position_x_temp, position_y_temp);
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
                WindowsMethods.updateWindow(windowManager, floatImageView_Edit, bitmap_Edit, touchable_edit, zoom, picture_degree, position_x_temp, position_y_temp);
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
                    WindowsMethods.updateWindow(windowManager, floatImageView_Edit, bitmap_Edit, touchable_edit, zoom, picture_degree, position_x_temp, position_y_temp);
                } else {
                    Toast.makeText(getActivity(), R.string.settings_picture_position_warn, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        if (touchable_edit) {
            dialog.setNeutralButton(R.string.save_moved_position, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    position_x = (int) floatImageView_Edit.getMovedPositionX();
                    position_y = (int) floatImageView_Edit.getMovedPositionY();
                    onSuccessEditPicture(floatImageView_Edit, bitmap_Edit);
                }
            });
        }
        dialog.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                position_x = position_x_temp;
                position_y = position_y_temp;
                onSuccessEditPicture(floatImageView_Edit, bitmap_Edit);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFailedEditPicture(floatImageView_Edit, bitmap_Edit);
            }
        });
        dialog.setView(mView);
        dialog.show();
    }

    private void onEditPicture(FloatImageView FloatImageView_Edit) {
        if (!onUseEditPicture) {
            windowManager.removeView(floatImageView);
            floatImageView.refreshDrawableState();
            WindowsMethods.createWindow(windowManager, FloatImageView_Edit, touch_and_move, position_x, position_y);
            onUseEditPicture = true;
        }
    }

    private void onSuccessEditPicture(FloatImageView floatImageView_Edit, Bitmap bitmap_Edit) {
        if (onUseEditPicture) {
            windowManager.removeView(floatImageView_Edit);
            floatImageView_Edit.refreshDrawableState();
            bitmap_Edit.recycle();
            floatImageView.setImageBitmap(ImageMethods.resizeBitmap(bitmap, zoom, picture_degree));
            WindowsMethods.createWindow(windowManager, floatImageView, touch_and_move, position_x, position_y);
            onUseEditPicture = false;
        }
    }

    private void onFailedEditPicture(FloatImageView floatImageView_Edit, Bitmap bitmap_Edit) {
        if (onUseEditPicture) {
            windowManager.removeView(floatImageView_Edit);
            floatImageView_Edit.refreshDrawableState();
            bitmap_Edit.recycle();
            WindowsMethods.createWindow(windowManager, floatImageView, touch_and_move, position_x, position_y);
            onUseEditPicture = false;
        }
    }

    public void saveAllData() {
        pictureData.put(Config.DATA_PICTURE_SHOW_ENABLED, true);
        pictureData.put(Config.DATA_PICTURE_ZOOM, zoom);
        pictureData.put(Config.DATA_PICTURE_DEFAULT_ZOOM, default_zoom);
        pictureData.put(Config.DATA_PICTURE_ALPHA, picture_alpha);
        if (touch_and_move) {
            position_x = (int) floatImageView.getMovedPositionX();
            position_y = (int) floatImageView.getMovedPositionY();
        }
        pictureData.put(Config.DATA_PICTURE_POSITION_X, position_x);
        pictureData.put(Config.DATA_PICTURE_POSITION_Y, position_y);
        pictureData.put(Config.DATA_PICTURE_DEGREE, picture_degree);
        pictureData.put(Config.DATA_PICTURE_TOUCH_AND_MOVE, touch_and_move);
        pictureData.commit(PictureName);
        WindowsMethods.updateWindow(windowManager, floatImageView, bitmap, touch_and_move, zoom, picture_degree, position_x, position_y);
        ImageMethods.saveFloatImageViewById(getActivity(), PictureId, floatImageView);
    }

    public void clearEditView() {
        if (onUseEditPicture) {
            if (floatImageView_Edit != null && bitmap_Edit != null) {
                onFailedEditPicture(floatImageView_Edit, bitmap_Edit);
            }
        }
    }

    public void exit() {
        if (!Edit_Mode) {
            if (floatImageView != null) {
                windowManager.removeView(floatImageView);
                bitmap.recycle();
                floatImageView = null;
            }
            ImageMethods.clearAllTemp(getActivity(), PictureId);
        } else {
            float original_zoom = pictureData.getFloat(Config.DATA_PICTURE_ZOOM, zoom);
            float original_alpha = pictureData.getFloat(Config.DATA_PICTURE_ALPHA, picture_alpha);
            float original_degree = pictureData.getFloat(Config.DATA_PICTURE_DEGREE, picture_degree);
            int original_position_x = pictureData.getInt(Config.DATA_PICTURE_POSITION_X, position_x);
            int original_position_y = pictureData.getInt(Config.DATA_PICTURE_POSITION_Y, position_y);
            boolean original_touch_and_move = pictureData.getBoolean(Config.DATA_PICTURE_TOUCH_AND_MOVE, Config.DATA_DEFAULT_PICTURE_TOUCH_AND_MOVE);
            floatImageView.setAlpha(original_alpha);
            floatImageView.setMoveable(original_touch_and_move);
            WindowsMethods.updateWindow(windowManager, floatImageView, bitmap, original_touch_and_move, original_zoom, original_degree, original_position_x, original_position_y);
        }

    }

}
