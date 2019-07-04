package com.cjy.mvplibrary.presenter.base;

import android.support.annotation.UiThread;

import com.cjy.mvplibrary.R;
import com.cjy.mvplibrary.application.AppLibrary;
import com.cjy.mvplibrary.bridge.BridgeFactory;
import com.cjy.mvplibrary.bridge.Bridges;
import com.cjy.mvplibrary.bridge.sharepref.SharedPrefManager;
import com.cjy.mvplibrary.bridge.sharepref.SharedPrefUser;
import com.cjy.mvplibrary.utils.LogUtils;
import com.cjy.retrofitlibrary.observ.RetrofitHttp;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.WeakReference;

/**
 * <基础业务类>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BasePresenter<V extends IMvpView> implements Presenter<V> {

    private WeakReference<V> viewRef;

    private SecurityManager securityManager;

    private RetrofitHttp.Builder retrofitHttp;

    /**
     * 获取 View
     *
     * @return
     */
    @UiThread
    public V getMvpView() {
        return viewRef == null ? null : viewRef.get();
    }

    /**
     * 判断View是否已经添加
     *
     * @return
     */
    @UiThread
    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    /**
     * 绑定 View
     *
     * @param view
     */
    @UiThread
    @Override
    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }

    /**
     * 移除 View
     */
    @Override
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * MD5加密
     */
    protected SecurityManager getSecurityManager() {
        if (securityManager == null)
            securityManager = BridgeFactory.getBridge(Bridges.SECURITY);
        return securityManager;
    }

    /**
     * 网络请求
     */
    protected RetrofitHttp.Builder getRetrofitHttp() {

        if (!isViewAttached()) {
            LogUtils.d(AppLibrary.getAppString(R.string.view_add_error));
            return null;
        }

        if (retrofitHttp == null)
            retrofitHttp = BridgeFactory.getBridge(Bridges.HTTP);
        retrofitHttp.clear();
        retrofitHttp.lifecycle((LifecycleProvider) getMvpView());
        retrofitHttp.addHeader(SharedPrefUser.USER_TOKEN, SharedPrefManager.getUser()
                .getString(SharedPrefUser.USER_TOKEN, ""));
        return retrofitHttp;
    }
}
