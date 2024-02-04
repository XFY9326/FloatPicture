package tool.xfy9326.floatpicture.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;

import tool.xfy9326.floatpicture.Methods.CodeMethods;
import tool.xfy9326.floatpicture.Methods.IOMethods;

public class PictureData {

    private static final String DataFileName = "PictureData.list";
    private static final String ListFileName = "PictureList.list";
    private String id;
    private JSONObject detailObject;
    private JSONObject listObject;
    private JSONObject dataObject;

    public PictureData() {
    }

    public void setDataControl(String id) {
        this.id = id;
        this.listObject = getJSONFile(ListFileName);
        this.dataObject = getJSONFile(DataFileName);
        this.detailObject = getDetailObject(this.id);
    }

    @SuppressWarnings("SameParameterValue")
    public void put(String name, boolean value) {
        try {
            detailObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void put(String name, String value) {
        try {
            detailObject.put(name, CodeMethods.unicodeEncode(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(String name, int value) {
        try {
            detailObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("SameParameterValue")
    public void put(String name, float value) {
        try {
            detailObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("SameParameterValue")
    public boolean getBoolean(String name, boolean defaultValue) {
        if (detailObject.has(name)) {
            try {
                return detailObject.getBoolean(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    @SuppressWarnings("unused")
    public String getString(String name, String defaultValue) {
        if (detailObject.has(name)) {
            try {
                return CodeMethods.unicodeDecode(detailObject.getString(name));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public int getInt(String name, int defaultValue) {
        if (detailObject.has(name)) {
            try {
                return detailObject.getInt(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    @SuppressWarnings("SameParameterValue")
    public float getFloat(String name, float defaultValue) {
        if (detailObject.has(name)) {
            try {
                return Float.parseFloat(detailObject.get(name) + "f");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public void commit(String pictureName) {
        try {
            if (pictureName != null) {
                listObject.put(id, pictureName);
            }
            dataObject.put(id, detailObject);
            setJSONFile(ListFileName, listObject);
            setJSONFile(DataFileName, dataObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void remove() {
        if (listObject.has(id)) {
            listObject.remove(id);
            dataObject.remove(id);
            setJSONFile(ListFileName, listObject);
            setJSONFile(DataFileName, dataObject);
        }
    }

    private JSONObject getDetailObject(String id) {
        if (dataObject.has(id)) {
            try {
                return dataObject.getJSONObject(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

    public LinkedHashMap<String, String> getListArray() {
        JSONObject listObject = getJSONFile(ListFileName);
        try {
            Iterator<String> iterator = listObject.keys();
            LinkedHashMap<String, String> arr = new LinkedHashMap<>();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                arr.put(key, listObject.getString(key));
            }
            return arr;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getJSONFile(String FileName) {
        String content = IOMethods.readFile(Config.DEFAULT_DATA_DIR + FileName);
        if (content != null) {
            try {
                if (!content.isEmpty()) {
                    return new JSONObject(content);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean setJSONFile(String FileName, JSONObject jsonObject) {
        return IOMethods.writeFile(jsonObject.toString(), Config.DEFAULT_DATA_DIR + FileName);
    }

}
