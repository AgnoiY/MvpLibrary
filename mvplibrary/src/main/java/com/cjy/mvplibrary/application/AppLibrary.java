package com.cjy.mvplibrary.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;

import com.cjy.mvplibrary.bridge.BridgeFactory;
import com.cjy.mvplibrary.bridge.BridgeLifeCycleSetKeeper;
import com.cjy.mvplibrary.utils.LogUtils;
import com.cjy.mvplibrary.utils.ToastUtils;
import com.cjy.retrofitlibrary.RetrofitLibrary;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.cjy.mvplibrary.constant.Constants.IS_DEBUG;

/**
 * <应用初始化> <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class AppLibrary {

    AppLibrary() {
        throw new IllegalStateException("RetrofitLibrary class");
    }

    private static Application mApplication;

    /**
     * 本地Activity栈
     */
    private static List<Activity> activitys;

    /**
     * 当前Avtivity名称
     */
    private static String currentActivityName;

    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application, String baseUrl) {
        if (application != null) {
            mApplication = application;
            install(application);
            initData(baseUrl);
        }
    }

    /**
     * dex文件太大不能运行问题
     *
     * @param context
     */
    private static void install(Context context) {
        MultiDex.install(context);
    }

    /**
     * 初始化数据
     */
    private static void initData(String baseUrl) {
        BridgeFactory.init(RetrofitLibrary.init(mApplication, baseUrl));
        BridgeLifeCycleSetKeeper.getInstance().initOnApplicationCreate(mApplication);
        EventBus.builder().throwSubscriberException(IS_DEBUG).installDefaultEventBus();
    }

    /**
     * 退出应用，清理内存
     */
    public static void onDestory() {
        BridgeLifeCycleSetKeeper.getInstance().clearOnApplicationQuit();
        BridgeFactory.destroy();
        RetrofitLibrary.onDestory();
        ToastUtils.destory();
        if (activitys != null) {
            activitys.clear();
            activitys = null;
        }
    }


    /**
     * <添加> <功能详细描述>
     *
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public static void addActivity(Activity activity) {
        if (activitys == null) {
            activitys = new ArrayList<>();
        }
        activitys.add(activity);
    }

    /**
     * <删除>
     * <功能详细描述>
     *
     * @param o
     * @see [类、类#方法、类#成员]
     */
    public static void deleteActivity(Object o) {
        if (o != null)
            if (o instanceof Activity) {
                Activity activity = ((Activity) o);
                activitys.remove(activity);
                activity.finish();
            } else if (o instanceof Class) {
                try {
                    Activity activity = (Activity) ((Class) o).newInstance();
                    activitys.remove(activity);
                    activity.finish();
                } catch (Exception e) {
                    LogUtils.w(e);
                }
            }
    }

    /**
     * Finish全部Activity
     */
    public static void clearAllAcitity() {

        if (activitys == null) return;

        for (Activity activity : activitys) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activitys.clear();
    }

    public static String getAppString(@StringRes int resId) {
        return mApplication.getString(resId);
    }

    public static Application getApplication() {
        return mApplication;
    }

    public static List<Activity> getActivitys() {
        return activitys;
    }

    public static String getCurrentActivityName() {
        return currentActivityName;
    }

    public static void setCurrentActivityName(String currentActivityName) {
        AppLibrary.currentActivityName = currentActivityName;
    }
}
