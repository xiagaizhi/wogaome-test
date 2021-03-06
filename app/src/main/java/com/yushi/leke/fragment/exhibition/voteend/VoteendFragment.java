package com.yushi.leke.fragment.exhibition.voteend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.yufan.library.Global;
import com.yufan.library.api.ApiBean;
import com.yufan.library.api.ApiManager;
import com.yufan.library.api.YFListHttpCallBack;
import com.yufan.library.base.BaseListFragment;
import com.yufan.library.inject.VuClass;
import com.yufan.library.inter.ICallBack;
import com.yufan.library.view.recycler.PageInfo;
import com.yushi.leke.UIHelper;
import com.yushi.leke.YFApi;
import com.yushi.leke.fragment.exhibition.voteend.allproject.AllendFragment;
import com.yushi.leke.fragment.exhibition.win.WinlistDialogFragment;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by mengfantao on 18/8/2.
 */
@VuClass(VoteendVu.class)
public class VoteendFragment extends BaseListFragment<VoteendContract.IView> implements VoteendContract.Presenter {
    private MultiTypeAdapter adapter;
    private Voteendinfolist infolist;
    private ICallBack mICallBack;
    private String activityid;
    private int exhibitionType;
    VoteendBinder voteendBinder;
    public void setmICallBack(ICallBack mICallBack) {
        this.mICallBack = mICallBack;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            activityid = bundle.getString(Global.BUNDLE_KEY_ACTIVITYID);
            exhibitionType = bundle.getInt(Global.BUNDLE_KEY_EXHIBITION_TYE);
        }
        adapter = new MultiTypeAdapter();
        voteendBinder=new VoteendBinder(getContext(),new ICallBack() {
            @Override
            public void OnBackResult(Object... s) {
                if (mICallBack!=null){
                    mICallBack.OnBackResult(s[0], s[1], s[2]);
                }
            }
        });
        adapter.register(Voteendinfo.class, voteendBinder);
        vu.getRecyclerView().setAdapter(adapter);
        adapter.setItems(list);
        vu.getRecyclerView().getAdapter().notifyDataSetChanged();
        onRefresh();
        openWinPage();
    }

    @Override
    public void onLoadMore(int index) {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class)
                .getvoteenddata(index, activityid))
                .useCache(false)
                .enqueue(new YFListHttpCallBack(getVu()) {
                    @Override
                    public void onSuccess(ApiBean mApiBean) {
                        super.onSuccess(mApiBean);
                        if (!TextUtils.isEmpty(mApiBean.getData())) {
                            infolist = JSON.parseObject(mApiBean.getData(), Voteendinfolist.class);
                            if (infolist != null && infolist.getProjectList().size() > 0) {
                                list.addAll(infolist.getProjectList());
                                vu.getRecyclerView().getAdapter().notifyDataSetChanged();
                            } else {
                                vu.getRecyclerView().getPageManager().setPageState(PageInfo.PAGE_STATE_NO_MORE);
                            }
                        }
                    }
                });
    }


    @Override
    public void onRefresh() {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class)
                .getvoteenddata(1, activityid))
                .useCache(false)
                .enqueue(new YFListHttpCallBack(getVu()) {
                    @Override
                    public void onSuccess(ApiBean mApiBean) {
                        super.onSuccess(mApiBean);
                        if (!TextUtils.isEmpty(mApiBean.getData())) {
                            infolist = JSON.parseObject(mApiBean.getData(), Voteendinfolist.class);
                            if (infolist != null && infolist.getProjectList().size() > 0) {
                                list.clear();
                                list.addAll(infolist.getProjectList());
                                vu.getRecyclerView().getAdapter().notifyDataSetChanged();
                            } else {
                                vu.setStateEmpty();
                                vu.getRecyclerView().getPageManager().setPageState(PageInfo.PAGE_STATE_NO_MORE);
                            }
                        } else {
                            vu.setStateEmpty();
                        }
                    }
                });
    }


    @Override
    public void allprojectOnclick() {
        getRootFragment().start(UIHelper.creat(AllendFragment.class)
                .put(Global.BUNDLE_KEY_ACTIVITYID, activityid)
                .put(Global.BUNDLE_KEY_EXHIBITION_TYE, exhibitionType)
                .build());
    }

    @Override
    public void openWinPage() {
        WinlistDialogFragment winlistDialogFragment = new WinlistDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Global.BUNDLE_KEY_ACTIVITYID, activityid);
        winlistDialogFragment.setArguments(bundle);
        winlistDialogFragment.show(getFragmentManager(), "WinlistDialogFragment");
    }
}
