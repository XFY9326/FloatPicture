package tool.xfy9326.floatpicture;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

import tool.xfy9326.floatpicture.Tools.CrashHandler;

public class MainApplication extends Application {
    private HashMap<String, View> ViewRegister;
    private boolean ApplicationInit;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationInit = false;
        if (!BuildConfig.DEBUG) {
            CrashHandler.get().Catch(this);
        }
        this.ViewRegister = new HashMap<>();
    }

    public boolean isApplicationInit() {
        return ApplicationInit;
    }

    public void setApplicationInit(boolean init) {
        ApplicationInit = init;
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
            Object mView = ViewRegister.get(id);
            if (mView instanceof ImageView) {
                ((ImageView) mView).refreshDrawableState();
            }
            ViewRegister.remove(id);
            return true;
        }
        return false;
    }
}
