package com.cjy.mvplibrary.capabilities.http.observer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cjy.mvplibrary.R;
import com.cjy.mvplibrary.application.AppLibrary;
import com.cjy.mvplibrary.bridge.sharepref.SharedPrefManager;
import com.cjy.mvplibrary.capabilities.http.exception.ExceptionEngine;
import com.cjy.mvplibrary.capabilities.http.interfaces.ParseHelper;
import com.cjy.mvplibrary.utils.ToastUtils;

import org.json.JSONException;

/**
 * Http请求回调
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class HttpObserver<T> extends BaseBackObserver<T> implements ParseHelper<T> {

    public HttpObserver() {
    }

    public HttpObserver(Context context, boolean isDialog, boolean isCabcelble) {
        super(context, isDialog, isCabcelble);
    }

    /**
     * 是否回调成功函数
     */
    private boolean callSuccess = true;

    @Override
    public T parse(String data) {
        T t = null;
        try {
            t = onConvert(data);
            callSuccess = true;
        } catch (JSONException e) {
            callSuccess = false;
            onError(getTag(), ExceptionEngine.ANALYTIC_CLIENT_DATA_ERROR, AppLibrary.getAppString(R.string.data_parsing_error));
        }
        return t;
    }


    @Override
    public void inSuccess(String action, T value) {
        T result = parse((String) value);
        if (callSuccess && result != null) {
            onSuccess(action, result);
        }
    }

    @Override
    public void inError(String action, int code, String desc) {
        onError(action, code, desc);
    }

    @Override
    public void inCancel() {
        onCancel();
    }

    @Override
    public void isLoginToken() {
        SharedPrefManager.getUser().clear();
        Activity activity = AppLibrary.getActivitys().get(AppLibrary.getActivitys().size() - 1);
        AppLibrary.clearAllAcitity();
//        activity.startActivity(new Intent(activity, LoginActivity.class));
        ToastUtils.makeCenterToast(activity, AppLibrary.getAppString(R.string.login_elsewhere));
    }
}
