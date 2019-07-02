package com.cjy.mvplibrary.ui.base.fragment;

import com.cjy.mvplibrary.R;
import com.cjy.mvplibrary.databinding.LayoutRecyclerRefreshBinding;
import com.cjy.mvplibrary.view.recyclerview.RecyclerInterface;
import com.cjy.mvplibrary.view.recyclerview.RefreshHelper;

/**
 * 公用刷新
 * Data：2018/12/18
 *
 * @author yong
 */

public abstract class BaseRefreshListFragment<T> extends BaseLazyFragment<T, LayoutRecyclerRefreshBinding>
        implements RecyclerInterface<T> {


    protected RefreshHelper mRefreshHelper;

    @Override
    public int getLayoutId() {
        return R.layout.layout_recycler_refresh;
    }

    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper(int limit) {
        mRefreshHelper = initRefreshHelper(mLazyBinding.refreshLayout, mLazyBinding.rv, limit);
        mRefreshHelper.onDefaluteMRefresh();

    }

    @Override
    public void lazyLoad() {
        initRefreshHelper(setLimit());
    }

    /**
     * 设置一页的加载数据量，默认加载10个
     *
     * @return
     */
    public abstract int setLimit();
}
