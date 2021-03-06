package com.yushi.leke.fragment.register;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.alibaba.verificationsdk.ui.IActivityCallback;
import com.alibaba.verificationsdk.ui.VerifyActivity;
import com.alibaba.verificationsdk.ui.VerifyType;
import com.yufan.library.Global;
import com.yufan.library.api.ApiBean;
import com.yufan.library.api.ApiManager;
import com.yufan.library.api.BaseHttpCallBack;
import com.yufan.library.api.EnhancedCall;
import com.yufan.library.base.BaseFragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.yufan.library.inject.VuClass;
import com.yufan.library.manager.DialogManager;
import com.yufan.library.manager.SPManager;
import com.yufan.library.manager.UserManager;
import com.yufan.library.util.SoftInputUtil;
import com.yushi.leke.App;
import com.yushi.leke.UIHelper;
import com.yushi.leke.YFApi;
import com.yushi.leke.fragment.browser.BrowserBaseFragment;
import com.yushi.leke.fragment.login.LoginFragment;
import com.yushi.leke.fragment.main.MainFragment;
import com.yushi.leke.util.ArgsUtil;


import java.util.Map;

import okhttp3.Call;

/**
 * Created by mengfantao on 18/8/2.
 */
@VuClass(RegisterVu.class)
public class RegisterFragment extends BaseFragment<RegisterContract.IView> implements RegisterContract.Presenter {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    @Override
    public void onRefresh() {

    }



    @Override
    public void register(final String phone, String password, String verifcationCode) {
        DialogManager.getInstance().showLoadingDialog();
        EnhancedCall call= ApiManager.getCall(ApiManager.getInstance().create(YFApi.class).registerViaVcode(phone,password,verifcationCode,
                SPManager.getInstance().getString("XGTOKEN", "")));
      call.enqueue(new BaseHttpCallBack() {
          @Override
          public void onSuccess(ApiBean mApiBean) {
              JSONObject jsonObject= JSON.parseObject(mApiBean.getData());
              UserManager.getInstance().setToken(jsonObject.getString("token"));
              UserManager.getInstance().setUid(jsonObject.getString("uid"));
              App.getApp().bindUid(UserManager.getInstance().getUid());
              startWithPopTo(UIHelper.creat(MainFragment.class).build(), LoginFragment.class,true);
              //用户注册数据埋点
              ArgsUtil.getInstance().registerpoint(jsonObject.getString("uid"));
          }

          @Override
          public void onError(int id, Exception e) {

          }

          @Override
          public void onFinish() {
              DialogManager.getInstance().dismiss();
          }
      });
    }


    @Override
    public void getVerifcationCode(String phone, String sessionID) {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class).sendRegisterVcode(phone,sessionID)).enqueue(new BaseHttpCallBack() {
            @Override
            public void onSuccess(ApiBean mApiBean) {
                DialogManager.getInstance().toast("发送成功");
            }

            @Override
            public void onError(int id, Exception e) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    @Override
    public void onAgreementClick() {
        start(UIHelper.creat(BrowserBaseFragment.class).put(Global.BUNDLE_KEY_BROWSER_URL,ApiManager.getInstance().getApiConfig().getProtocol()).build());
    }
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SoftInputUtil.hideSoftInput(getActivity(),getView());
    }
}
