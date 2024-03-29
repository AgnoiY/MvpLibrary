package com.cjy.mvplibrary.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjy.mvplibrary.event.BaseEventModel;
import com.cjy.mvplibrary.presenter.BasePresenter;
import com.cjy.mvplibrary.presenter.IMvpView;
import com.cjy.mvplibrary.ui.PresentationLayerFuncHelper;
import com.cjy.mvplibrary.ui.delegate.FragmentMvpDelegate;
import com.cjy.mvplibrary.ui.delegate.FragmentMvpDelegateImpl;
import com.cjy.mvplibrary.ui.interfaces.CreateInit;
import com.cjy.mvplibrary.ui.interfaces.PresentationLayerFunc;
import com.cjy.mvplibrary.ui.interfaces.PublishActivityCallBack;
import com.cjy.mvplibrary.utils.GeneralUtils;
import com.cjy.mvplibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.disposables.CompositeDisposable;

import static com.cjy.mvplibrary.constant.Constants.LOG_D;
import static com.cjy.mvplibrary.constant.Constants.LOG_V;


/**
 * * 备注:
 * 1.XXFragment 继承 MvpFragment,当页面存在 Presenter 时，具体 Fragment 需要调用 setPresenter(P... presenter)
 * 2.支持一个 Fragment 存在多个 Presenter
 *
 * @param <T>
 * @param <V>
 * @param <P> Data：2018/12/18
 * @author yong
 */

public abstract class BaseFragment<T, V extends IMvpView, P extends BasePresenter<V>> extends RxFragment
        implements CreateInit.CreateInitFragment<V, P>, PublishActivityCallBack, PresentationLayerFunc<T>,
        IMvpView<T>, View.OnClickListener {

    protected FragmentMvpDelegate mvpDelegate;

    private PresentationLayerFuncHelper helper;

    protected CompositeDisposable disposable;

    public static final String TAG = BaseFragment.class.getClass().getSimpleName();

    /**
     * Activity对象
     */
    protected Activity mActivity;

    /**
     * 此方法可以得到上下文对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        getMvpDelegate().onCreate(savedInstanceState);
    }

    /**
     * 返回一个需要展示的View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initView(inflater);
    }

    /**
     * 当Activity初始化之后可以在这里进行一些数据的初始化操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMvpDelegate().onActivityCreated(savedInstanceState);

        disposable = new CompositeDisposable();
        helper = new PresentationLayerFuncHelper(mActivity, disposable);
        EventBus.getDefault().register(this);

        initData();
        initListeners();
    }

    /**
     * 链接Presenter
     *
     * @return
     */
    @Override
    public P[] createPresenter() {
        return getPresenterArray();
    }

    /**
     * 注入View
     *
     * @return
     */
    @Override
    public V[] createView() {
        V[] views = null;
        P[] pArray = createPresenter();
        if (pArray != null) {
            views = (V[]) new IMvpView[pArray.length];
            for (int i = 0; i < pArray.length; i++) {
                views[i] = (V) this;
            }
        }
        return views;
    }

    /**
     * 关联Fragment的生命周期
     *
     * @return
     */
    @NonNull
    protected FragmentMvpDelegate getMvpDelegate() {
        if (mvpDelegate == null) {
            mvpDelegate = new FragmentMvpDelegateImpl(this, this);
        }
        return mvpDelegate;
    }

    /**
     * 发送EventBus事件线
     *
     * @param o
     */
    @Override
    public void getEventBusPost(Object... o) {
        helper.getEventBusPost(o);
    }

    /**
     * 处理事件线
     *
     * @param eventModel
     */
    @Subscribe
    @Override
    public void onEventMainThread(BaseEventModel<T> eventModel) {
        helper.onEventMainThread(eventModel);
    }

    /**
     * 配合DataBinding点击事件监听
     * 添加防止重复点击
     * 有点击事件只需重写
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (GeneralUtils.isDoubleClick()) return;
        log("", LOG_V);
    }

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        helper.startActivity(openClass, bundle);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        helper.openActivityForResult(openClass, requestCode, bundle);
    }

    @Override
    public void setResultOk(Bundle bundle) {
        helper.setResultOk(bundle);
    }

    @Override
    public void showToast(String msg) {
        helper.showToast(msg);
    }

    @Override
    public void showSoftKeyboard(View focusView) {
        helper.showSoftKeyboard(focusView);
    }

    @Override
    public void hideSoftKeyboard() {
        helper.hideSoftKeyboard();
    }

    @Override
    public void postDelayed(Object... delay) {
        helper.postDelayed(delay);
    }

    @Override
    public void nextStep(Long l, Object... delay) {
        log(l, LOG_D);
    }

    @Override
    public void log(Object... msg) {
        helper.log(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMvpDelegate().onDestroyView();
        ToastUtils.destory();
        EventBus.getDefault().unregister(this);
        if (disposable != null) {
            disposable.dispose();
            disposable.clear();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMvpDelegate().onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMvpDelegate().onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        getMvpDelegate().onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMvpDelegate().onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        getMvpDelegate().onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        getMvpDelegate().onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getMvpDelegate().onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getMvpDelegate().onSaveInstanceState(outState);
    }
}
