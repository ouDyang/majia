package com.app.majia.utils;

import android.content.Context;

/**
 * @author Administrator
 * @date 2017/5/17
 */

public class AppContext {

    public static final String APP_ID = "ea2e53bdc9114d3487e71081ff61c6e2";
    public static final String KEY = "4406aef39d51477c";
    public static Context appContext;
    public static String VERSION_NAME;
    public static int VERSION_CODE;
    public static String CHANNEL;
    private static AppContext appInstance;
    public static boolean LOG_SHOW = true;//控制Log是否打印, true表示打印反之不打印

    private AppContext(Context mContext) {
        appContext = mContext;
    }

    public static void instance(Context context) {
        if (appInstance == null) {
            synchronized (AppContext.class) {
                if (appInstance == null) {
                    appInstance = new AppContext(context);
                    initApp();
                }
            }
        }
    }

    public static void initApp() {
        VERSION_NAME = AppUtils.getAppVersionName();
        VERSION_CODE = AppUtils.getAppVersionCode();
        CHANNEL = AppUtils.getUmengChannel(appContext);
    }
}
