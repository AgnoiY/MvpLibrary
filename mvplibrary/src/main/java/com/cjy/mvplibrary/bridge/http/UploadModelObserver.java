package com.cjy.mvplibrary.bridge.http;

import android.content.Context;

import com.cjy.mvplibrary.presenter.base.BasePresenter;
import com.cjy.mvplibrary.view.dialog.UITipDialog;
import com.cjy.retrofitlibrary.UploadObserver;

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
public abstract class UploadModelObserver<T> extends UploadObserver<T> {

    private BasePresenter mPresenter;

    public UploadModelObserver(BasePresenter mPresenter) {
        this(mPresenter, false);
    }

    /**
     * @param presenter
     * @param isDialog  是否显示加载进度对话框
     */
    public UploadModelObserver(BasePresenter presenter, boolean isDialog) {
        super((Context) presenter.getMvpView(), isDialog);
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
