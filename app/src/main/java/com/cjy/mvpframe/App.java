package com.cjy.mvpframe;

import android.app.Application;

import com.cjy.mvplibrary.application.MvpLibrary;

/**
 * <应用初始化> <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MvpLibrary.init(this, UrlConstans.BASESERVER);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        MvpLibrary.onDestory();
    }
}
