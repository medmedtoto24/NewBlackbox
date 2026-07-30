package top.niunaijun.blackbox.fake.service;

import java.lang.reflect.Method;

import top.niunaijun.blackbox.fake.hook.ClassInvocationStub;
import top.niunaijun.blackbox.fake.hook.MethodHook;
import top.niunaijun.blackbox.fake.hook.ProxyMethod;
import top.niunaijun.blackbox.utils.Slog;

public class CameraManagerProxy extends ClassInvocationStub {
    public static final String TAG = "CameraManagerProxy";

    public CameraManagerProxy() {
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

    @ProxyMethod("getCameraIdList")
    public static class GetCameraIdList extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "CameraManager.getCameraIdList blocked - returning empty array");
                return new String[0];
            }
            Slog.d(TAG, "CameraManager.getCameraIdList() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getCameraCharacteristics")
    public static class GetCameraCharacteristics extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "CameraManager.getCameraCharacteristics blocked");
                throw new RuntimeException("Camera access blocked by security policy");
            }
            Slog.d(TAG, "CameraManager.getCameraCharacteristics() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("openCamera")
    public static class OpenCamera extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "CameraManager.openCamera blocked by protection policy");
                throw new RuntimeException("Camera access blocked by security policy");
            }
            Slog.d(TAG, "CameraManager.openCamera() called, allowing");
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("registerAvailabilityCallback")
    public static class RegisterAvailabilityCallback extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                Slog.d(TAG, "CameraManager.registerAvailabilityCallback blocked - skipping");
                return null;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("unregisterAvailabilityCallback")
    public static class UnregisterAvailabilityCallback extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("registerTorchCallback")
    public static class RegisterTorchCallback extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                return null;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("setTorchMode")
    public static class SetTorchMode extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (CameraProtectionManager.get().shouldBlockCamera()) {
                return null;
            }
            return method.invoke(who, args);
        }
    }
}
