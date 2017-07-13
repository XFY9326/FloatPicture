package tool.xfy9326.floatpicture;

import android.app.Application;
import android.view.View;

import java.util.HashMap;

import tool.xfy9326.floatpicture.Tools.CrashHandler;

public class MainApplication extends Application {
    private HashMap<String, View> ViewRegister;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            CrashHandler.get().Catch(this);
        }
        this.ViewRegister = new HashMap<>();
    }

    public void registerView(String id, View mView) {
        ViewRegister.put(id, mView);
    }

    public HashMap<String, View> getRegister() {
        return ViewRegister;
    }

    public View getRegisteredView(String id) {
        if (ViewRegister.containsKey(id)) {
            return ViewRegister.get(id);
        }
        return null;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean unregisterView(String id) {
        if (ViewRegister.containsKey(id)) {
            ViewRegister.remove(id);
            return true;
        }
        return false;
    }
}
