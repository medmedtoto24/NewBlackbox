package top.niunaijun.blackbox.fake.service;

import android.content.Context;
import android.content.SharedPreferences;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.utils.Slog;

public class CameraProtectionManager {

    public static final int METHOD_DISABLE_CAMERA = 1;
    public static final int METHOD_LOCAL_VIDEO = 2;
    public static final int METHOD_NETWORK_VIDEO = 3;

    private static final CameraProtectionManager sInstance = new CameraProtectionManager();
    private static final String PREFS_NAME = "camera_protection";
    private SharedPreferences mPrefs;

    public static CameraProtectionManager get() {
        return sInstance;
    }

    private SharedPreferences getPrefs() {
        if (mPrefs == null) {
            try {
                Context ctx = BlackBoxCore.getContext();
                if (ctx != null) {
                    mPrefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                }
            } catch (Exception e) {
                Slog.w("CameraProtectionManager", "Failed to get prefs: " + e.getMessage());
            }
        }
        return mPrefs;
    }

    public boolean isEnabled() {
        SharedPreferences prefs = getPrefs();
        return prefs == null || prefs.getBoolean("enabled", true);
    }

    public void setEnabled(boolean enabled) {
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            prefs.edit().putBoolean("enabled", enabled).apply();
        }
    }

    public int getMethodType() {
        SharedPreferences prefs = getPrefs();
        return prefs != null ? prefs.getInt("method_type", METHOD_DISABLE_CAMERA) : METHOD_DISABLE_CAMERA;
    }

    public void setMethodType(int type) {
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            prefs.edit().putInt("method_type", type).apply();
        }
    }

    public boolean shouldBlockCamera() {
        if (!isEnabled()) {
            return false;
        }
        return getMethodType() == METHOD_DISABLE_CAMERA;
    }
}
