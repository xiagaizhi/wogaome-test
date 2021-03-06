package com.yushi.leke.fragment.exhibition.voteing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.yushi.leke.fragment.exhibition.vote.VoteFragment;
import com.yushi.leke.fragment.exhibition.voteing.allproject.AllprojectsFragment;
import com.yushi.leke.util.ArgsUtil;
import com.yushi.leke.fragment.paySafe.PaySafetyFragment;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by mengfantao on 18/8/2.
 */
@VuClass(VoteingVu.class)
public class VoteingFragment extends BaseListFragment<VoteingContract.IView> implements VoteingContract.Presenter, ICallBack {
    private MultiTypeAdapter adapter;
    private Voteinginfolist infolist;
    private ICallBack mICallBack;
    private String activityid;
    private Voteinginfo choiceVoteinginfo;
    private int exhibitionType;
    VoteingBinder voteingBinder;
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
        voteingBinder=new VoteingBinder(getContext(),new ICallBack() {
            @Override
            public void OnBackResult(Object... s) {
                if (mICallBack != null) {
                    int type = (int) s[0];
                    if (type == 1) {
                        choiceVoteinginfo = (Voteinginfo) s[1];
                        String projectId = choiceVoteinginfo.getId();
                        VoteFragment voteFragment = new VoteFragment();
                        voteFragment.setmICallBack(VoteingFragment.this);
                        Bundle args = new Bundle();
                        args.putString(Global.BUNDLE_PROJECT_ID, projectId);
                        voteFragment.setArguments(args);
                        voteFragment.show(getFragmentManager(), "VoteFragment");
                    } else if (type == 2) {
                        mICallBack.OnBackResult(s[1], s[2], s[3]);
                    }

                }
            }
        });
        adapter.register(Voteinginfo.class, voteingBinder);
        vu.getRecyclerView().setAdapter(adapter);
        adapter.setItems(list);
        vu.getRecyclerView().getAdapter().notifyDataSetChanged();
        onRefresh();
    }

    @Override
    public void onLoadMore(int index) {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class)
                .getvoteingdata(index, activityid))
                .useCache(false)
                .enqueue(new YFListHttpCallBack(getVu()) {
                    @Override
                    public void onSuccess(ApiBean mApiBean) {
                        super.onSuccess(mApiBean);
                        if (!TextUtils.isEmpty(mApiBean.getData())) {
                            infolist = JSON.parseObject(mApiBean.getData(), Voteinginfolist.class);
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
                .getvoteingdata(1, activityid))
                .useCache(false)
                .enqueue(new YFListHttpCallBack(getVu()) {
                    @Override
                    public void onSuccess(ApiBean mApiBean) {
                        super.onSuccess(mApiBean);
                        if (!TextUtils.isEmpty(mApiBean.getData())) {
                            infolist = JSON.parseObject(mApiBean.getData(), Voteinginfolist.class);
                            if (infolist != null && infolist.getProjectList().size() > 0) {
                                list.clear();
                                if (mICallBack != null) {//请求播放第一条视频
                                    Voteinginfo voteinginfo = infolist.getProjectList().get(0);
                                    mICallBack.OnBackResult(voteinginfo.getAliVideoId(), voteinginfo.getTitle(), voteinginfo.getId());
                                    voteingBinder.flag=true;

                                }
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
        getRootFragment().start(UIHelper
                .creat(AllprojectsFragment.class)
                .put(Global.BUNDLE_KEY_ACTIVITYID, activityid)
                .put(Global.BUNDLE_KEY_EXHIBITION_TYE, exhibitionType)
                .build());
    }

    @Override
    public void OnBackResult(Object... s) {
        int type = (int) s[0];
        switch (type) {
            case 1:
                getRootFragment().start(UIHelper.creat(PaySafetyFragment.class).build());
                break;
            case 2:
                long voteNum = (long) s[1];
                if (choiceVoteinginfo != null) {
                    choiceVoteinginfo.setVotes(choiceVoteinginfo.getVotes() + voteNum);
                    getVu().getRecyclerView().getAdapter().notifyDataSetChanged();
                }
                break;
        }

    }
}
