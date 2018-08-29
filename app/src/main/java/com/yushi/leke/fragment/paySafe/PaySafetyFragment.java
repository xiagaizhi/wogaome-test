package com.yushi.leke.fragment.paySafe;

import android.os.Bundle;

import com.yufan.library.api.ApiBean;
import com.yufan.library.api.ApiManager;
import com.yufan.library.api.BaseHttpCallBack;
import com.yufan.library.base.BaseFragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.yufan.library.inject.VuClass;
import com.yushi.leke.UIHelper;
import com.yushi.leke.YFApi;
import com.yushi.leke.dialog.recharge.SetRechargePwdDialog;
import com.yushi.leke.fragment.bindPhone.BindPhoneFragment;

import org.json.JSONObject;

/**
 * Created by mengfantao on 18/8/2.
 */
@VuClass(PaySafetyVu.class)
public class PaySafetyFragment extends BaseFragment<PaySafetyContract.IView> implements PaySafetyContract.Presenter, SetRechargePwdDialog.ReturnPwdState {
    private int isHave;
    private String phoneNumber;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class).haveTradePwd("v1", "9999"))
                .useCache(false)
                .enqueue(new BaseHttpCallBack() {
                    @Override
                    public void onSuccess(ApiBean mApiBean) {
                        try {
                            if (TextUtils.equals(ApiBean.SUCCESS, mApiBean.getCode())) {
                                String data = mApiBean.getData();
                                JSONObject jsonObject = new JSONObject(data);
                                isHave = jsonObject.optInt("isHave");
                                phoneNumber = jsonObject.optString("phoneNumber");
                                getVu().updatePage(isHave, phoneNumber);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
    public void onRefresh() {

    }

    @Override
    public void openBindPhone() {
        startForResult(UIHelper.creat(BindPhoneFragment.class).put("isNeedReturnState", true).build(), 100);
    }

    @Override
    public void checkPhone(String phoneNumber) {
        startForResult(UIHelper.creat(BindPhoneFragment.class).put("isNeedReturnState", true).put("isSafetyCheck", true).put("phoneNumber", phoneNumber).build(), 100);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // TODO: 2018/8/28 刷新界面
        }
    }

    @Override
    public void setRechargePwd(int isHavePwd) {
        SetRechargePwdDialog setRechargePwdDialog;
        if (isHavePwd == 1) {
            setRechargePwdDialog = new SetRechargePwdDialog(getContext(), SetRechargePwdDialog.CHECK_ADN_MODIFY_RECHARGE_PWD);
        } else {
            setRechargePwdDialog = new SetRechargePwdDialog(getContext(), SetRechargePwdDialog.SET_RECHARGE_PWD);
        }
        setRechargePwdDialog.setReturnPwdState(this);
        setRechargePwdDialog.show();
    }

    @Override
    public void toSetPwdReturnState() {
        isHave = 1;
        getVu().updatePage(isHave, phoneNumber);
    }

    @Override
    public void toModifiyPwdReturnState() {
        //修改密码校验通过,设置交易密码
        SetRechargePwdDialog setRechargePwdDialog = new SetRechargePwdDialog(getContext(), SetRechargePwdDialog.SET_RECHARGE_PWD_NEW);
        setRechargePwdDialog.setReturnPwdState(this);
        setRechargePwdDialog.show();
    }
}
