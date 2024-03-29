package com.cjy.mvplibrary.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.cjy.mvplibrary.R;
import com.cjy.mvplibrary.application.AppLibrary;
import com.cjy.mvplibrary.event.BaseEventModel;
import com.cjy.mvplibrary.presenter.BasePresenter;
import com.cjy.mvplibrary.presenter.IMvpView;
import com.cjy.mvplibrary.ui.PresentationLayerFuncHelper;
import com.cjy.mvplibrary.ui.delegate.ActivityMvpDelegate;
import com.cjy.mvplibrary.ui.delegate.ActivityMvpDelegateImpl;
import com.cjy.mvplibrary.ui.interfaces.CreateInit;
import com.cjy.mvplibrary.ui.interfaces.PresentationLayerFunc;
import com.cjy.mvplibrary.ui.interfaces.PublishActivityCallBack;
import com.cjy.mvplibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.disposables.CompositeDisposable;

import static com.cjy.mvplibrary.constant.Constants.LOG_D;
import static com.cjy.mvplibrary.constant.Constants.LOG_W;

/**
 * * 备注:
 * 1.XXActivity 继承 BaseActivity,当页面存在 Presenter 时，具体 Activity 需要调用 setPresenter(P... presenter)
 * 2.支持一个 Activity 存在多个 Presenter
 *
 * @param <T>
 * @param <V>
 * @param <P> Data：2018/12/18
 * @author yong
 */
public abstract class BaseActivity<T, V extends IMvpView, P extends BasePresenter<V>> extends RxAppCompatActivity implements
        CreateInit.CreateInitActivity<V, P>, PublishActivityCallBack, PresentationLayerFunc<T>, IMvpView<T>, View.OnClickListener {

    private ActivityMvpDelegate mvpDelegate;

    private PresentationLayerFuncHelper helper;

    protected CompositeDisposable disposable;

    public static final String TAG = BaseActivity.class.getClass().getSimpleName();

    /**
     * Context对象
     */
    protected Context mContext;

    /**
     * Activity对象
     */
    protected Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMvpDelegate().onCreate(savedInstanceState);

        disposable = new CompositeDisposable();
        helper = new PresentationLayerFuncHelper<T>(this, disposable);

        setContentView(savedInstanceState);
        mContext = this;
        mActivity = this;
        getWindow().setBackgroundDrawableResource(R.color.transparent);//移除布局根背景
        AppLibrary.addActivity(this);
        EventBus.getDefault().register(this);

        initData();
        initStatusBarDarkTheme();
        initListeners();
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
     * 关联Activity的生命周期
     *
     * @return
     */
    @NonNull
    protected ActivityMvpDelegate getMvpDelegate() {
        if (mvpDelegate == null) {
            mvpDelegate = new ActivityMvpDelegateImpl(this, this);
        }
        return mvpDelegate;
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

    @Override
    protected void onResume() {
        AppLibrary.setCurrentActivityName(this.getClass().getName());
        getMvpDelegate().onResume();
        super.onResume();
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

    /**
     * 隐藏软键盘
     */
    @Override
    public void hideSoftKeyboard() {
        helper.hideSoftKeyboard();
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
     * 延迟操作
     *
     * @param delay
     */
    @Override
    public void postDelayed(Object... delay) {
        helper.postDelayed(delay);
    }

    /**
     * 延迟后要进行的操作
     *
     * @param l
     */
    @Override
    public void nextStep(Long l, Object... delay) {
        log(l, LOG_D);
    }

    /**
     * 打印日志
     *
     * @param msg 0: 信息  1: 日志的样式 e/d/i
     */
    @Override
    public void log(Object... msg) {
        helper.log(msg);
    }

    @Override
    protected void onDestroy() {
        getMvpDelegate().onDestroy();
        AppLibrary.deleteActivity(this);
        EventBus.getDefault().unregister(this);
        ToastUtils.destory();
        if (disposable != null) {
            disposable.dispose();
            disposable.clear();
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        try {
            helper.hideSoftKeyboard();
        } catch (Exception e) {
            log(e, LOG_W, "finish 输入法错误");
        }
        super.finish();
    }

    /**
     * 监听点击事件 实现点击页面上除EditView外的位置隐藏输入法
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    helper.hideKeyboard(v.getWindowToken());
                }
            }
        } catch (Exception e) {
            log(e, LOG_W, "dispatchTouchEvent 输入法错误");
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0];
            int top = l[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            boolean isEvent = event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom;
            return !isEvent;

        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getMvpDelegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getMvpDelegate().onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMvpDelegate().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getMvpDelegate().onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMvpDelegate().onRestart();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        getMvpDelegate().onContentChanged();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getMvpDelegate().onPostCreate(savedInstanceState);
    }
}
