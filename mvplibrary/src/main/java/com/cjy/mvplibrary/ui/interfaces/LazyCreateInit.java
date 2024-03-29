package com.cjy.mvplibrary.ui.interfaces;

/**
 * <功能详细描述>
 * <p>
 * Data：2019/01/15
 *
 * @author yong
 */
public interface LazyCreateInit {

    /**
     * fragment显示时调用
     */
    void lazyLoad();

    /**
     * fragment隐藏时调用
     */
    void onInvisible();
}
