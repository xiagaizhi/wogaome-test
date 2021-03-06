package com.yushi.leke.fragment.register;

import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yufan.library.inject.FindView;
import com.yufan.library.util.CheckUtil;
import com.yufan.library.util.SoftInputUtil;
import com.yufan.library.widget.LoginLineView;
import com.yushi.leke.R;
import com.yufan.library.base.BaseVu;
import com.yufan.library.inject.FindLayout;
import com.yufan.library.inject.Title;
import com.yufan.library.widget.StateLayout;
import com.yufan.library.widget.AppToolbar;
import com.yushi.leke.widget.VerificationCodeTextView;

import java.util.Map;

/**
 * Created by mengfantao on 18/8/2.
 */
@FindLayout(layout = R.layout.fragment_layout_register)
@Title("注册")
public class RegisterVu extends BaseVu<RegisterContract.Presenter> implements RegisterContract.IView, View.OnClickListener {

    @FindView(R.id.et_phone)
    EditText et_phone;
    @FindView(R.id.et_password)
    EditText et_password;
    @FindView(R.id.cb_showeye)
    CheckBox cb_showeye;
    @FindView(R.id.et_verification_code)
    EditText et_verification_code;
    @FindView(R.id.tv_vcode)
    VerificationCodeTextView verificationCodeTextView;
    @FindView(R.id.bt_register)
    Button bt_register;
    @FindView(R.id.tv_agreement)
    TextView tv_agreement;
    @FindView(R.id.iv_clear_password)
    ImageView iv_clear_password;
    @FindView(R.id.iv_clear_phone)
    ImageView iv_clear_phone;
    @FindView(R.id.line_view1)
    LoginLineView line_view1;
    @FindView(R.id.line_view2)
    LoginLineView line_view2;
    @FindView(R.id.line_view3)
    LoginLineView line_view3;
    @FindView(R.id.ll_content)
    LinearLayout ll_content;
    @Override
    public void initView(View view) {
        tv_agreement.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        iv_clear_password.setOnClickListener(this);
        iv_clear_phone.setOnClickListener(this);
        verificationCodeTextView.setOnGetCodeClickListener(new VerificationCodeTextView.OnGetCodeClickListener() {
            @Override
            public void getCode(String  sessionId) {
                mPersenter.getVerifcationCode(et_phone.getText().toString(),sessionId);
            }

            @Override
            public String getPhone() {
                return et_phone.getText().toString();
            }
        });
        cb_showeye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_password.requestFocus();
                } else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password.requestFocus();

                }
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s)){
                    iv_clear_password.setVisibility(View.GONE);
                }else {
                    updateState();
                    iv_clear_password.setVisibility(View.VISIBLE);
                }
            }
        });
        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(!TextUtils.isEmpty( et_password.getText())){
                        //显示
                        iv_clear_password.setVisibility(View.VISIBLE);
                    }
                    line_view2.startAnim();
                }else {
                    //隐藏
                    iv_clear_password.setVisibility(View.GONE);
                    if(TextUtils.isEmpty( et_password.getText())){
                        line_view2.hintAnim();
                    }
                }

            }
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s)){
                    iv_clear_phone.setVisibility(View.GONE);
                }else {
                    updateState();
                    iv_clear_phone.setVisibility(View.VISIBLE);
                }
            }
        });
        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(!TextUtils.isEmpty( et_phone.getText())){
                        //显示
                        iv_clear_phone.setVisibility(View.VISIBLE);
                    }
                    line_view1.startAnim();
                }else {
                    //隐藏
                    iv_clear_phone.setVisibility(View.GONE);
                    if(TextUtils.isEmpty( et_phone.getText())){
                        line_view1.hintAnim();
                    }
                }
            }
        });
        et_verification_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    line_view3.startAnim();
                }else {
                    //隐藏
                    if(TextUtils.isEmpty( et_verification_code.getText())){
                        line_view3.hintAnim();
                    }
                }
            }
        });
        et_verification_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateState();
            }
        });
        ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInputUtil.hideSoftInput(getContext(),getView());
            }
        });
    }
    private void updateState(){
        if(CheckUtil.checkInputEmpty(et_phone,et_password,et_verification_code,false)){
            bt_register.setEnabled(true);
        }else {
            bt_register.setEnabled(false);
        }
    }

    @Override
    public boolean initTitle(AppToolbar appToolbar) {
        ImageView imageView= appToolbar.creatLeftView(ImageView.class);
        imageView.setImageResource(com.yufan.library.R.drawable.left_back_black_arrows);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPersenter.onBackPressed();
            }
        });
        appToolbar.build();
        return true;
    }

    @Override
    public void initStatusLayout(StateLayout stateLayout) {
        super.initStatusLayout(stateLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_agreement:
                mPersenter.onAgreementClick();
                break;
            case R.id.bt_register:
                if(CheckUtil.checkInputState(et_phone,et_password,et_verification_code,true)){
                    mPersenter.register(et_phone.getText().toString(),et_password.getText().toString(),et_verification_code.getText().toString());
                }

                break;

            case R.id.iv_clear_password:
                et_password.setText("");
                break;

            case R.id.iv_clear_phone:
               et_phone.setText("");

                break;


        }
    }
}
