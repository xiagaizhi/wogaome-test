package com.yushi.leke.dialog.recharge;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yufan.library.api.ApiBean;
import com.yufan.library.api.ApiManager;
import com.yufan.library.api.BaseHttpCallBack;
import com.yufan.library.manager.DialogManager;
import com.yufan.library.widget.customkeyboard.KeyboardAdapter;
import com.yufan.library.widget.customkeyboard.KeyboardView;
import com.yufan.library.widget.customkeyboard.PayPsdInputView;
import com.yushi.leke.R;
import com.yushi.leke.YFApi;
import com.yushi.leke.dialog.CommonDialog;
import com.yushi.leke.util.RechargeUtil;

import java.lang.reflect.Method;

/**
 * Created by zhanyangyang on 18/8/24.
 */

public class SetRechargePwdDialog extends Dialog implements KeyboardAdapter.OnKeyboardClickListener {
    public static final int SET_RECHARGE_PWD_BYTOKEN = 1;
    public static final int FORGET_RECHARGE_PWD_BYTOKEN = 2;
    public static final int SET_RECHARGE_PWD_BYOLDPWD = 3;
    private PayPsdInputView tv_password;
    private KeyboardView id_keyboard_view;
    private TextView mTitle;
    private Context mContext;
    private int type;//true:设置／修改交易密码 false:输入支付密码进行验证
    private TextView mSetRechargeType;
    private RechargeUtil.SetRechargeInterf mSetRechargeInterf;
    private String token;


    public void setmSetRechargeInterf(RechargeUtil.SetRechargeInterf setRechargeInterf) {
        this.mSetRechargeInterf = setRechargeInterf;
    }

    public SetRechargePwdDialog(@NonNull Context context, final int type, String token) {
        super(context);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_setrechargepassword, null);
        setContentView(rootView);
        this.mContext = context;
        this.type = type;
        this.token = token;
        initView(rootView);
    }

    private void initView(View rootView) {
        mSetRechargeType = rootView.findViewById(R.id.tv_recharge_type);
        mTitle = rootView.findViewById(R.id.tv_setpwd_title);
        rootView.findViewById(R.id.id_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        rootView.findViewById(R.id.id_top_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_password = rootView.findViewById(R.id.tv_password);
        id_keyboard_view = rootView.findViewById(R.id.id_keyboard_view);
        setCanceledOnTouchOutside(false);
        if (type == SET_RECHARGE_PWD_BYTOKEN) {
            mSetRechargeType.setText("设置交易密码");
            mTitle.setText("请设置您的乐客APP的交易密码");
        } else if (type == SET_RECHARGE_PWD_BYOLDPWD) {
            mSetRechargeType.setText("修改交易密码");
            mTitle.setText("请设置您的新交易密码");
        } else if (type == FORGET_RECHARGE_PWD_BYTOKEN) {
            mSetRechargeType.setText("忘记交易密码");
            mTitle.setText("请设置您的乐客APP的交易密码");
        }
        //设置不调用系统键盘
        if (Build.VERSION.SDK_INT <= 10) {
            tv_password.setInputType(InputType.TYPE_NULL);
        } else {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(tv_password, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!id_keyboard_view.isVisible()) {
                    id_keyboard_view.show();
                }
            }
        });
        id_keyboard_view.setOnKeyBoardClickListener(this);
        tv_password.setComparePassword(new PayPsdInputView.onPasswordListener() {
            @Override
            public void onDifference(String oldPsd, String newPsd) {
                //和上次输入的密码不一致  做相应的业务逻辑处理
                tv_password.setComparePassword("");
                tv_password.cleanPsd();
                if (type == SET_RECHARGE_PWD_BYTOKEN) {
                    mTitle.setText("请设置您的乐客APP的交易密码");
                } else if (type == SET_RECHARGE_PWD_BYOLDPWD) {
                    mTitle.setText("请设置您的新交易密码");
                } else if (type == FORGET_RECHARGE_PWD_BYTOKEN) {
                    mTitle.setText("请设置您的乐客APP的交易密码");
                }
                new CommonDialog(mContext).setTitle("两次交易密码不一致，请重新输入")
                        .setPositiveName("确定")
                        .setHaveNegative(false)
                        .setCommonClickListener(new CommonDialog.CommonDialogClick() {
                            @Override
                            public void onClick(CommonDialog commonDialog, int actionType) {
                                commonDialog.dismiss();
                            }
                        }).show();

            }

            @Override
            public void onEqual(String psd) {
                //两次输入密码相同，去设置密码
                setRechargePwd(psd);
                tv_password.setComparePassword("");
                tv_password.cleanPsd();
            }

            @Override
            public void inputFinished(String inputPsd) {
                //输完逻辑
                tv_password.setComparePassword(inputPsd);
                tv_password.cleanPsd();
                mTitle.setText("请再输入一次您的交易密码");
            }
        });
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        window.setAttributes(p);
        window.setDimAmount(0.6f);
        window.setWindowAnimations(R.style.AnimBottomDialog);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onKeyClick(String data) {
        tv_password.setText(tv_password.getText().toString().trim() + data);
        tv_password.setSelection(tv_password.getText().length());
    }

    @Override
    public void onDeleteClick() {
        // 点击删除按钮
        String num = tv_password.getText().toString().trim();
        if (num.length() > 0) {
            tv_password.setText(num.substring(0, num.length() - 1));
            tv_password.setSelection(tv_password.getText().length());
        }
    }

    private boolean isSuccess;

    private void setRechargePwd(String pwd) {
        DialogManager.getInstance().showLoadingDialog();
        isSuccess = false;
        if (type == SET_RECHARGE_PWD_BYTOKEN || type == FORGET_RECHARGE_PWD_BYTOKEN) {
            setRechargePwdByToken(token, pwd);
        } else if (type == SET_RECHARGE_PWD_BYOLDPWD) {
            setRechargePwdByOriginalPwd(token, pwd);
        }
    }

    private BaseHttpCallBack baseHttpCallBack = new BaseHttpCallBack() {
        @Override
        public void onSuccess(ApiBean mApiBean) {
            isSuccess = true;
        }

        @Override
        public void onError(int id, Exception e) {
            isSuccess = false;
        }

        @Override
        public void onFinish() {
            dismiss();
            DialogManager.getInstance().dismiss();
            if (mSetRechargeInterf != null) {
                mSetRechargeInterf.returnSetPwdResult(isSuccess);
            }
            if (isSuccess) {
                if (type == SET_RECHARGE_PWD_BYOLDPWD) {
                    DialogManager.getInstance().toast("交易密码修改成功");
                } else {
                    DialogManager.getInstance().toast("交易密码设置成功");
                }
            } else {
                if (type == SET_RECHARGE_PWD_BYOLDPWD) {
                    DialogManager.getInstance().toast("交易密码修改失败");
                } else {
                    DialogManager.getInstance().toast("交易密码设置失败");
                }
            }
        }
    };


    /**
     * 通过绑定手机返回token(初次绑定手机)
     */
    private void setRechargePwdByToken(String token, String pwd) {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class).setTradePwdWithToken(token, pwd)).useCache(false).enqueue(baseHttpCallBack);
    }


    /**
     * 通过输入原有密码(修改密码) 后面改为通过token修改
     */
    private void setRechargePwdByOriginalPwd(String token, String pwd) {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class).modifyTradePwd(token, pwd)).useCache(false).enqueue(baseHttpCallBack);
    }
}
