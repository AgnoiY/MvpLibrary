package com.cjy.mvpframe;

import com.cjy.mvplibrary.bridge.http.ModelObserver;
import com.cjy.mvplibrary.presenter.BasePresenter;
import com.cjy.mvplibrary.presenter.IMvpView;

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
                .request(new ModelObserver<LoginModel>(this, true) {
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
                .request(new ModelObserver<LoginModel>(this, isRefresh) {
                });

    }

}
