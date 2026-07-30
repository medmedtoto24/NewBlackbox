package top.niunaijun.blackbox.fake.service;

import android.os.IBinder;

import black.android.os.BRServiceManager;
import top.niunaijun.blackbox.fake.hook.BinderInvocationStub;
import top.niunaijun.blackbox.utils.Slog;

public class ICameraServiceProxy extends BinderInvocationStub {
    public static final String TAG = "ICameraServiceProxy";

    public ICameraServiceProxy() {
        super(getCameraService());
    }

    private static IBinder getCameraService() {
        try {
            return BRServiceManager.get().getService("media.camera");
        } catch (Exception e) {
            Slog.w(TAG, "Failed to get camera service: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected Object getWho() {
        return getCameraService();
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        if (baseInvocation != null) {
            replaceSystemService("media.camera");
            Slog.d(TAG, "Camera service hooked successfully");
        }
    }

    @Override
    public boolean isBadEnv() {
        IBinder current = getCameraService();
        return current != null && current != getProxyInvocation();
    }
}
