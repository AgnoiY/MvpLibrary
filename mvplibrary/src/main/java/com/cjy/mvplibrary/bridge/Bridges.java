
package com.cjy.mvplibrary.bridge;

/**
 * <与Bridge模块一一对应，用以在BridgeFactory获取某个Bridge对应的Key>
 * Data：2018/12/18
 *
 * @author yong
 */
public class Bridges {

    private Bridges() {
        throw new IllegalStateException("Bridges class");
    }

    /**
     * 本地缓存(sd卡存储和手机内部存储)
     */
    public static final String LOCAL_FILE_STORAGE = "com.cjy.mvplibrary.LOCAL_FILE_STORAGE";

    /**
     * SharedPreference缓存
     */
    public static final String SHARED_PREFERENCE = "com.cjy.mvplibrary.SHARED_PREFERENCE";

    /**
     * 安全
     */
    public static final String SECURITY = "com.cjy.mvplibrary.SECURITY";

    /**
     * 用户Session
     */
    public static final String USER_SESSION = "com.cjy.mvplibrary.USER_SESSION";

    /**
     * CoreService，主要维护功能模块
     */
    public static final String CORE_SERVICE = "com.cjy.mvplibrary.CORE_SERVICE";


    /**
     * 数据库模块
     */
    public static final String DATABASE = "com.cjy.mvplibrary.DATABASE";

    /**
     * http请求
     */
    public static final String HTTP = "com.cjy.mvplibrary.HTTP";

}
