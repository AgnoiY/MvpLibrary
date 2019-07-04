package com.cjy.mvplibrary.application;

import android.app.Application;

/**
 * <应用初始化> <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class MvpLibrary {

    MvpLibrary() {
        throw new IllegalStateException("MvpLibrary class");
    }

    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application, String baseUrl) {
        if (application != null) {
            AppLibrary.init(application, baseUrl);
        }
    }

    /**
     * 退出应用，清理内存
     */
    public static void onDestory() {
        AppLibrary.onDestory();
    }
}
