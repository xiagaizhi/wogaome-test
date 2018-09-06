package com.yushi.leke.fragment.bindPhone;

import android.os.Bundle;

import com.yufan.library.api.ApiBean;
import com.yufan.library.api.ApiManager;
import com.yufan.library.api.BaseHttpCallBack;
import com.yufan.library.base.BaseFragment;
import com.yufan.library.inject.VuClass;
import com.yufan.library.manager.DialogManager;
import com.yushi.leke.YFApi;
import com.yushi.leke.dialog.CommonDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhanyangyang on 18/8/25.
 */
@VuClass(BindPhoneVu.class)
public class BindPhoneFragment extends BaseFragment<BindPhoneContract.IView> implements BindPhoneContract.Presenter {

    @Override
    public void onRefresh() {

    }

    @Override
    public void getVerifcationCode(String phone) {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class).sendBindMobileVcode(phone))
                .useCache(false)
                .enqueue(new BaseHttpCallBack() {
                    @Override
                    public void onSuccess(ApiBean mApiBean) {

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
    public void bindPhone(final String phone, String code, String pwd) {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class).bindMobile(phone, code, pwd))
                .useCache(false)
                .enqueue(new BaseHttpCallBack() {
                    @Override
                    public void onResponse(ApiBean mApiBean) {
                        String code = mApiBean.getCode();
                        if (ApiBean.checkOK(code)) {
                            String token = "";
                            try {
                                JSONObject jsonObject = new JSONObject(mApiBean.getData());
                                token = jsonObject.getString("token");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            DialogManager.getInstance().toast("操作成功");
                            Bundle bundle = new Bundle();
                            bundle.putString("token", token);
                            bundle.putString("phoneNumber", phone);
                            setFragmentResult(RESULT_OK, bundle);
                            pop();
                        } else {
                            new CommonDialog(_mActivity).setTitle("" + mApiBean.getMessage())
                                    .setNegativeName("取消")
                                    .setPositiveName("确定")
                                    .setCommonClickListener(new CommonDialog.CommonDialogClick() {
                                        @Override
                                        public void onClick(CommonDialog commonDialog, int actionType) {
                                            commonDialog.dismiss();
                                            pop();
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(int id, Exception e) {
                        super.onFailure(id, e);
                    }

                    @Override
                    public void onSuccess(ApiBean mApiBean) {

                    }

                    @Override
                    public void onError(int id, Exception e) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });

    }
}
