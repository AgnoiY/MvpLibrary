package com.cjy.mvplibrary.bridge;

import android.app.Application;

import com.cjy.mvplibrary.bridge.localstorage.LocalFileStorageManager;
import com.cjy.mvplibrary.bridge.security.SecurityManager;
import com.cjy.mvplibrary.bridge.sharepref.SharedPrefManager;
import com.cjy.retrofitlibrary.observ.RetrofitHttp;

import java.util.HashMap;

/**
 * <中间连接层>
 * Data：2018/12/18
 *
 * @author yong
 */
public class BridgeFactory {

    private static BridgeFactory model;

    private HashMap<String, Object> mBridges;

    private BridgeFactory() {
        mBridges = new HashMap<>();
    }

    public static void init(RetrofitHttp.Builder builder) {
        model = new BridgeFactory();
        model.iniLocalFileStorageManager();
        model.initPreferenceManager();
        model.initSecurityManager();
        model.initUserSession();
        model.initOkHttpManager(builder);
    }

    public static void destroy() {
        model.mBridges = null;
        model = null;
    }

    /**
     * 初始化本地存储路径管理类
     */
    private void iniLocalFileStorageManager() {
        LocalFileStorageManager localFileStorageManager = new LocalFileStorageManager();
        model.mBridges.put(Bridges.LOCAL_FILE_STORAGE, localFileStorageManager);
        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(localFileStorageManager);
    }

    /**
     * 初始化SharedPreference管理类
     */
    private void initPreferenceManager() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager();
        model.mBridges.put(Bridges.SHARED_PREFERENCE, sharedPrefManager);
        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(sharedPrefManager);
    }

    /**
     * 网络请求管理类
     */
    private void initOkHttpManager(RetrofitHttp.Builder builder) {
        model.mBridges.put(Bridges.HTTP, builder);
    }

    /**
     * 初始化安全模块
     */
    private void initSecurityManager() {
        SecurityManager securityManager = new SecurityManager();
        model.mBridges.put(Bridges.SECURITY, securityManager);
        BridgeLifeCycleSetKeeper.getInstance().trustBridgeLifeCycle(securityManager);
    }

    /**
     * 初始化用户信息模块
     */
    private void initUserSession() {
        // Initialize user information module
    }

    /**
     * 通过bridgeKey {@link Bridges}来获取对应的Bridge模块
     *
     * @param bridgeKey {@link Bridges}
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <V extends Object> V getBridge(String bridgeKey) {
        final Object bridge = model.mBridges.get(bridgeKey);
        if (bridge == null) {
            throw new NullPointerException("-no defined bridge-");
        }
        return (V) bridge;
    }
}
