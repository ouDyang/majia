package com.app.majia.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.app.majia.BuildConfig;
import com.app.majia.utils.AppContext;
import com.app.majia.utils.DeviceUtils;
import com.app.majia.utils.LogUtil;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        String model = DeviceUtils.getModel();
        int sdkInt = Build.VERSION.SDK_INT;
        LogUtil.v("model==="+model);
        LogUtil.v("sdkInt==="+sdkInt);
        if (model.toLowerCase().contains("oppo") && sdkInt < 24) {
            fuckOppo();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppContext.instance(this);

        // JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);


        UMConfigure.setLogEnabled(BuildConfig.DEBUG);

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);

    }

    /**
     * 修复oppo 6.0及其以下的手机 runtime bug
     */
    public void fuckOppo() {
        try {
            final Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            final Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            final Object watchdog = field.get(null);
            try {
                final Field thread = clazz.getSuperclass().getDeclaredField("thread");
                thread.setAccessible(true);
                thread.set(watchdog, null);
            } catch (final Throwable t) {
                LogUtil.e("stopWatchDog, set null occur error:" + t);

                t.printStackTrace();
                try {
                    // 直接调用stop方法，在Android 6.0之前会有线程安全问题
                    final Method method = clazz.getSuperclass().getDeclaredMethod("stop");
                    method.setAccessible(true);
                    method.invoke(watchdog);
                } catch (final Throwable e) {
                    LogUtil.e("stopWatchDog, stop occur error:" + t);
                    t.printStackTrace();
                }
            }
        } catch (final Throwable t) {
            LogUtil.e("stopWatchDog, get object occur error:" + t);
            t.printStackTrace();
        }
    }
}
