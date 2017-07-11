package tool.xfy9326.floatpicture.Utils;

import android.os.Environment;

import java.io.File;

public class Config {
    public final static int REQUEST_CODE_PERMISSION_STORAGE = 1;
    public final static int REQUEST_CODE_PERMISSION_OVERLAY = 2;
    public final static int REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_ADD = 3;
    public final static int REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_GET_PICTURE = 4;
    public final static int REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_CHANGE = 5;

    public final static String INTENT_PICTURE_EDIT_POSITION = "EDIT_POSITION";
    public final static String INTENT_PICTURE_EDIT_ID = "EDIT_ID";
    public final static String INTENT_PICTURE_EDIT_MODE = "EDIT_MODE";
    public final static String INTENT_PICTURE_CHOOSE_PICTURE = "CHOOSE_PICTURE";

    public final static String DATA_PICTURE_SHOW_ENABLED = "SHOW_ENABLED";
    public final static String DATA_PICTURE_POSITION_X = "POSITION_X";
    public final static String DATA_PICTURE_POSITION_Y = "POSITION_Y";
    public final static String DATA_PICTURE_ZOOM = "ZOOM";
    public final static String DATA_PICTURE_IS_GIF = "IS_GIF";

    public final static boolean DATA_DEFAULT_PICTURE_SHOW_ENABLED = true;
    public final static String DATA_DEFAULT_PICTURE_NAME = "New Picture";
    public final static int DATA_DEFAULT_PICTURE_POSITION_X = 100;
    public final static int DATA_DEFAULT_PICTURE_POSITION_Y = 100;
    public final static boolean DATA_DEFAULT_PICTURE_IS_GIF = false;

    public final static String PREFERENCE_PICTURE_NAME = "settings_picture_name";
    public final static String PREFERENCE_PICTURE_ID = "settings_picture_id";
    public final static String PREFERENCE_PICTURE_RESIZE = "settings_picture_resize";
    public final static String PREFERENCE_PICTURE_POSITION = "settings_picture_position";

    private final static String DEFAULT_ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private final static String DEFAULT_APPLICATION_DIR = DEFAULT_ROOT_DIR + "FloatPicture" + File.separator;
    public final static String DEFAULT_PICTURE_DIR = DEFAULT_APPLICATION_DIR + "Pictures" + File.separator;
    final static String DEFAULT_DATA_DIR = DEFAULT_APPLICATION_DIR + "Data" + File.separator;

}
