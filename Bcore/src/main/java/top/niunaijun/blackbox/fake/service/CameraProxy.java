package top.niunaijun.blackbox.fake.service;

import android.hardware.Camera;

import java.lang.reflect.Method;

import top.niunaijun.blackbox.fake.hook.ClassInvocationStub;
import top.niunaijun.blackbox.fake.hook.MethodHook;
import top.niunaijun.blackbox.fake.hook.ProxyMethod;
import top.niunaijun.blackbox.utils.Slog;

public class CameraProxy extends ClassInvocationStub {
    public static final String TAG = "CameraProxy";

    public CameraProxy() {
        super();
    }

    @Override
    protected Object getWho() {
        return null;
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("open")
    public static class Open extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "Camera blocked by protection policy");
                throw new RuntimeException("Camera access blocked by security policy");
            }
            Slog.d(TAG, "Camera.open() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("openLegacy")
    public static class OpenLegacy extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "Camera blocked by protection policy");
                throw new RuntimeException("Camera access blocked by security policy");
            }
            Slog.d(TAG, "Camera.openLegacy() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getNumberOfCameras")
    public static class GetNumberOfCameras extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "Camera count blocked by protection policy");
                return 0;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("startPreview")
    public static class StartPreview extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "Camera startPreview blocked by protection policy");
                return null;
            }
            Slog.d(TAG, "Camera.startPreview() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("stopPreview")
    public static class StopPreview extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Slog.d(TAG, "Camera.stopPreview() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("takePicture")
    public static class TakePicture extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "Camera takePicture blocked by protection policy");
                return null;
            }
            Slog.d(TAG, "Camera.takePicture() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setPreviewCallback")
    public static class SetPreviewCallback extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "Camera setPreviewCallback blocked, setting null");
                args[0] = null;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setOneShotPreviewCallback")
    public static class SetOneShotPreviewCallback extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "Camera setOneShotPreviewCallback blocked, setting null");
                args[0] = null;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("release")
    public static class Release extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Slog.d(TAG, "Camera.release() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setPreviewDisplay")
    public static class SetPreviewDisplay extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Slog.d(TAG, "Camera.setPreviewDisplay() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setPreviewTexture")
    public static class SetPreviewTexture extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Slog.d(TAG, "Camera.setPreviewTexture() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("autoFocus")
    public static class AutoFocus extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Slog.d(TAG, "Camera.autoFocus() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setDisplayOrientation")
    public static class SetDisplayOrientation extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Slog.d(TAG, "Camera.setDisplayOrientation() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getParameters")
    public static class GetParameters extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Slog.d(TAG, "Camera.getParameters() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setParameters")
    public static class SetParameters extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "Camera setParameters blocked");
                return null;
            }
            Slog.d(TAG, "Camera.setParameters() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("<init>")
    public static class Constructor extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Slog.d(TAG, "Camera constructor called, allowing");
            return method.invoke(who, args);
        }
    }
}
