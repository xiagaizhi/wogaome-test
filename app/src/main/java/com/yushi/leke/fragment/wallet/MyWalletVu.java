package com.yushi.leke.fragment.wallet;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yufan.library.Global;
import com.yufan.library.base.BasePopupWindow;
import com.yufan.library.inject.AnnotateUtils;
import com.yufan.library.inject.FindView;
import com.yufan.library.manager.SPManager;
import com.yufan.library.util.PxUtil;
import com.yufan.library.util.StringUtil;
import com.yufan.library.widget.highlightview.HighLightInfo;
import com.yufan.library.widget.highlightview.HighLightView;
import com.yushi.leke.R;
import com.yufan.library.base.BaseVu;
import com.yufan.library.inject.FindLayout;
import com.yufan.library.inject.Title;
import com.yufan.library.widget.StateLayout;
import com.yufan.library.widget.AppToolbar;
import com.yushi.leke.UIHelper;

/**
 * Created by mengfantao on 18/8/2.
 */
@FindLayout(layout = R.layout.fragment_layout_mywallet)
@Title("我的钱包")
public class MyWalletVu extends BaseVu<MyWalletContract.Presenter> implements MyWalletContract.IView {
    @FindView(R.id.id_lck_instructions)
    ImageView id_lck_instructions;
    @FindView(R.id.id_lkc_remain)
    TextView id_lkc_remain;
    @FindView(R.id.id_lkc_detail)
    TextView id_lkc_detail;
    @FindView(R.id.id_yesterd_arith_num)
    TextView id_yesterd_arith_num;
    @FindView(R.id.id_open_treasure)
    ImageButton id_open_treasure;
    @FindView(R.id.tv_power)
    TextView tv_power;
    private HighLightView highLightView;
    private ImageView keyIcon;
    private View rootView;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BasePopupWindow popupWindow = new BasePopupWindow(getContext());
            highLightView = new HighLightView(getContext(), popupWindow);
            HighLightInfo highLightInfo = new HighLightInfo(keyIcon, R.drawable.jt_up, HighLightInfo.ALIGN_LEFT_DOWN, HighLightInfo.HEIGHTLIGHT_ROUNDREC);
            highLightInfo.setOfftX(PxUtil.convertDIP2PX(getContext(), 25));
            highLightInfo.setOfftY(PxUtil.convertDIP2PX(getContext(), -22));
            highLightView.addHighLightInfo(0, highLightInfo);
            popupWindow.addView(highLightView);
            popupWindow.showAtLocation(rootView, Gravity.NO_GRAVITY, 0, 0);
        }
    };

    @Override
    public void initView(View view) {
        rootView = view;
        tv_power.setText("");
        tv_power.setVisibility(View.GONE);
        id_lkc_remain.setText(StringUtil.getMoneySpannble("0",getContext()));
        id_lck_instructions.setOnClickListener(this);
        id_lkc_detail.setOnClickListener(this);
        id_yesterd_arith_num.setOnClickListener(this);
        id_open_treasure.setOnClickListener(this);
        if (SPManager.getInstance().getBoolean(Global.SP_KEY_NEW_GUIDE, true)) {//是新手
            mHandler.sendMessageDelayed(mHandler.obtainMessage(), 400);
            SPManager.getInstance().saveValue(Global.SP_KEY_NEW_GUIDE, false);
        }
    }

    @Override
    public boolean initTitle(AppToolbar appToolbar) {
        String titleNameStr = AnnotateUtils.getTitle(this);
        if (!TextUtils.isEmpty(titleNameStr)) {
            TextView titleName = appToolbar.creatCenterView(TextView.class);
            titleName.setText(titleNameStr);
        }
        ImageView leftView = appToolbar.creatLeftView(ImageView.class);
        leftView.setImageResource(com.yufan.library.R.drawable.left_back_white_arrows);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPersenter.onBackPressed();
            }
        });



        keyIcon = appToolbar.creatRightView(ImageView.class);
        keyIcon.setImageResource(R.drawable.ic_toolbar_key);
        keyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPersenter.openPaySafety();
            }
        });

        appToolbar.build(false);
        return true;
    }

    @Override
    public void initStatusLayout(StateLayout stateLayout) {
        super.initStatusLayout(stateLayout);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_lck_instructions://lkc说明页
                mPersenter.openBrowserPage("lkc说明页");
                break;
            case R.id.id_lkc_detail://lkc明细
                mPersenter.openBrowserPage("lkc明细");
                break;
            case R.id.id_yesterd_arith_num://昨日算力
                mPersenter.openBrowserPage("昨日算力");
                break;
            case R.id.id_open_treasure://点击开宝箱
                mPersenter.openTreasureBox();
                break;
        }
    }

    @Override
    public void upDataMyWallet(MyWalletInfo myWalletInfo) {
        if (myWalletInfo != null) {
            id_lkc_remain.setText(StringUtil.getMoneySpannble(myWalletInfo.getLkc(),getContext()));
            if (myWalletInfo.getPower() > 0) {
                tv_power.setVisibility(View.VISIBLE);
                tv_power.setText(String.valueOf(myWalletInfo.getPower()));
            } else {
                tv_power.setVisibility(View.GONE);
            }
        }
    }
}
