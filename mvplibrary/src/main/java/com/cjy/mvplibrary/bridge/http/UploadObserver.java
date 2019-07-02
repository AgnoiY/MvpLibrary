package com.cjy.mvplibrary.bridge.http;

import android.content.Context;

import com.cjy.mvplibrary.R;
import com.cjy.mvplibrary.application.AppLibrary;
import com.cjy.mvplibrary.capabilities.http.interfaces.UploadProgressCallback;
import com.cjy.mvplibrary.capabilities.http.observer.HttpObserver;
import com.cjy.mvplibrary.utils.LogUtils;
import com.cjy.mvplibrary.view.dialog.UITipDialog;

import java.io.File;

/**
 * 上传回调接口
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class UploadObserver<T> extends HttpObserver<T> implements UploadProgressCallback {

    public UploadObserver() {
    }

    public UploadObserver(Context context, boolean isDialog, boolean isCabcelble) {
        super(context, isDialog, isCabcelble);
    }

    @Override
    public void progress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile) {
        onProgress(file, currentSize, totalSize, progress, currentIndex, totalFile);
    }

    /**
     * 上传回调
     *
     * @param file
     * @param currentSize
     * @param totalSize
     * @param progress
     * @param currentIndex
     * @param totalFile
     */
    public abstract void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile);

    /**
     * 失败回调
     *
     * @param action
     * @param code
     * @param desc
     */
    public void onError(String action, int code, String desc) {
        UITipDialog.showFall(AppLibrary.getApplication(), desc);
    }

    /**
     * 取消回调
     */
    public void onCancel() {
        LogUtils.d(AppLibrary.getAppString(R.string.request_cancelled));
    }

}
