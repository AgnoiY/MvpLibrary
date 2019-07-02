package com.cjy.mvpframe;

import com.cjy.mvplibrary.bridge.http.BaseModelObserver;
import com.cjy.mvplibrary.constant.UrlConstans;
import com.cjy.mvplibrary.presenter.base.BasePresenter;
import com.cjy.mvplibrary.presenter.base.IMvpView;

import java.util.HashMap;
import java.util.Map;

/**
 * <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class LoginPresenter extends BasePresenter<IMvpView> {

    public void login(String userid, String pwd) {

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", userid);
        map.put("password", pwd);

        getRetrofitHttp().post().apiUrl(UrlConstans.LOGIN)
                .addParameter(map).build()
                .request(new BaseModelObserver<LoginModel>(this) {
                });
    }


    /**
     * 短信记录列表搜索查询
     */
    public void getSmsRecord(boolean isRefresh, int pageNum, int pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", pageNum);
        map.put("pageSize", pageSize);

        getRetrofitHttp().post().apiUrl(UrlConstans.LIST)
                .addParameter(map).build()
                .request(new BaseModelObserver<LoginModel>(this, isRefresh) {
                });

    }

}
