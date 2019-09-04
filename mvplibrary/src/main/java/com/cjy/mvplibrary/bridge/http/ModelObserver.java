package com.cjy.mvplibrary.bridge.http;

import android.content.Context;

import com.cjy.mvplibrary.presenter.base.BasePresenter;
import com.cjy.mvplibrary.view.dialog.UITipDialog;
import com.cjy.retrofitlibrary.HttpObserver;

import java.util.List;


/**
 * 根据业务进一步封装
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 * <p>
 * 重要提醒 : abstract 不能给删掉
 */
public abstract class ModelObserver<T> extends HttpObserver<T> {

    private BasePresenter mPresenter;

    public ModelObserver(BasePresenter presenter) {
        this(presenter, false, false);
    }

    public ModelObserver(BasePresenter presenter, boolean isDialog) {
        this(presenter, isDialog, false);
    }

    /**
     * @param presenter
     * @param isDialog    是否显示加载进度对话框
     * @param isCabcelble 当返回键按下是否关闭加载进度对话框
     */
    public ModelObserver(BasePresenter presenter, boolean isDialog, boolean isCabcelble) {
        super((Context) presenter.getMvpView(), isDialog, isCabcelble);
        this.mPresenter = presenter;
    }

    /**
     * 网络请求的错误信息
     * 如果有特殊处理需重写
     *
     * @param action 区分不同事件
     * @param code   错误码
     * @param desc   错误信息
     */
    public void onError(String action, int code, String desc) {
        super.onError(action, code, desc);
        UITipDialog.showFall((Context) mPresenter.getMvpView(), desc);
        mPresenter.getMvpView().onError(action, code, desc);
    }


    @Override
    public void onSuccess(String action, T value) {
        if (value instanceof List) {
            mPresenter.getMvpView().onSuccess(action, (List<T>) value);
        } else {
            mPresenter.getMvpView().onSuccess(action, value);
        }
    }
}
