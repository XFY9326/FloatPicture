package tool.xfy9326.floatpicture;

import android.app.Application;
import android.view.View;

import java.util.HashMap;

import tool.xfy9326.floatpicture.Tools.CrashHandler;
import tool.xfy9326.floatpicture.View.FloatImageView;
import tool.xfy9326.floatpicture.View.ManageListAdapter;

public class MainApplication extends Application {
    private HashMap<String, View> ViewRegister;
    private ManageListAdapter manageListAdapter;
    private boolean ApplicationInit;
    private boolean winVisible = true;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationInit = false;
        if (!BuildConfig.DEBUG) {
            CrashHandler.get().Catch(this);
        }
        this.ViewRegister = new HashMap<>();
    }

    public boolean getWinVisible() {
        return winVisible;
    }

    public void setWinVisible(boolean visible) {
        winVisible = visible;
    }

    public ManageListAdapter getManageListAdapter() {
        return manageListAdapter;
    }

    public void setManageListAdapter(ManageListAdapter manageListAdapter) {
        this.manageListAdapter = manageListAdapter;
    }

    public boolean isAppInit() {
        return !ApplicationInit;
    }

    public void setAppInit(boolean init) {
        ApplicationInit = init;
    }

    public void registerView(String id, View mView) {
        ViewRegister.put(id, mView);
    }

    public HashMap<String, View> getRegister() {
        return ViewRegister;
    }

    public int getViewCount() {
        return ViewRegister.size();
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
            if (mView instanceof FloatImageView) {
                ((FloatImageView) mView).refreshDrawableState();
            }
            ViewRegister.remove(id);
            return true;
        }
        return false;
    }
}
