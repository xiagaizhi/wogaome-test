package com.yushi.leke.fragment.test;

import android.view.View;
import android.widget.TextView;

import com.yufan.library.base.BaseListVu;
import com.yufan.library.inject.FindLayout;
import com.yufan.library.inject.FindView;
import com.yufan.library.inject.Title;
import com.yufan.library.view.recycler.YFRecyclerView;
import com.yufan.library.widget.AppToolbar;
import com.yushi.leke.R;


/**
 * Created by mengfantao on 18/7/2.
 */

@Title("测试标题")
@FindLayout(layout = R.layout.layout_fragment_list)
public class TestVu extends BaseListVu<DbTestContract.Presenter> implements DbTestContract.View {
    @FindView(R.id.recyclerview)
    private YFRecyclerView mYFRecyclerView;
    /**
     * 初始化title
     * @param toolbar
     * @return
     */
    @Override
    public boolean initTitle(AppToolbar toolbar) {
        super.initTitle(toolbar);
      TextView tvInsert=  toolbar.creatRightView(TextView.class);
        tvInsert.setText("分享");
        tvInsert.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              mPersenter.share();

          }
      });
        TextView tv2=  toolbar.creatRightView(TextView.class);
        tv2.setText("音乐播放");
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPersenter.batchinsert();

            }
        });

        TextView tvUpdate=  toolbar.creatRightView(TextView.class);
        tvUpdate.setText("播放器");
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPersenter.startPlayer();
            }
        });

        TextView myWallet=  toolbar.creatRightView(TextView.class);
        myWallet.setText("我的钱包");
        myWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPersenter.openMyWallet();
            }
        });

        toolbar.build();
        return true;
    }



    @Override
    public YFRecyclerView getRecyclerView() {
        return mYFRecyclerView;
    }





}
