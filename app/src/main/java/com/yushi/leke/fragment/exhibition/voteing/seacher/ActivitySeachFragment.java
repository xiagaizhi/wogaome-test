package com.yushi.leke.fragment.exhibition.voteing.seacher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
import com.yushi.leke.fragment.exhibition.voteend.allproject.AllendBinder;
import com.yushi.leke.fragment.exhibition.voteing.allproject.AllprojectBinder;
import com.yushi.leke.fragment.exhibition.voteing.allproject.AllprojectsFragment;
import com.yushi.leke.fragment.exhibition.voteing.allproject.Allprojectsinfo;
import com.yushi.leke.fragment.exhibition.voteing.allproject.Allprojectsinfolist;
import com.yushi.leke.fragment.paySafe.PaySafetyFragment;
import com.yushi.leke.util.ArgsUtil;

import me.drakeet.multitype.MultiTypeAdapter;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by mengfantao on 18/8/2.
 */
@VuClass(ActivitySeachVu.class)
public class ActivitySeachFragment extends BaseListFragment<ActivitySeachContract.IView> implements ActivitySeachContract.Presenter ,ICallBack{
    private MultiTypeAdapter adapter;
    private String activityid;
    private int exhibitionType;
    private Allprojectsinfo choiceinfo;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            activityid = bundle.getString(Global.BUNDLE_KEY_ACTIVITYID);
            exhibitionType=bundle.getInt(Global.BUNDLE_KEY_EXHIBITION_TYE);
        }
        init();
    }
    private void init() {
        adapter = new MultiTypeAdapter();
        switch (exhibitionType) {
            case 2:
                //注册活动中
                adapter.register(Allprojectsinfo.class, new AllprojectBinder(new ICallBack() {
                    @Override
                    public void OnBackResult(Object... s) {
                        int type = (int) s[0];
                        if (type == 1) {
                            choiceinfo = (Allprojectsinfo) s[1];
                            String projectId = choiceinfo.getId();
                            VoteFragment voteFragment = new VoteFragment();
                            voteFragment.setmICallBack(ActivitySeachFragment.this);
                            Bundle args = new Bundle();
                            args.putString(Global.BUNDLE_PROJECT_ID, projectId);
                            voteFragment.setArguments(args);
                            voteFragment.show(getFragmentManager(), "VoteFragment");
                        }
                    }
                }));
                break;
            case 3:
                //注册活动已结束
                adapter.register(Allprojectsinfo.class,new AllendBinder(new ICallBack() {
                    @Override
                    public void OnBackResult(Object... s) {

                    }
                }));
                break;
        }
        vu.getRecyclerView().setAdapter(adapter);
        adapter.setItems(list);
        vu.getRecyclerView().getAdapter().notifyDataSetChanged();
        vu.setStateEmpty();
    }
    @Override
    public void onLoadMore(int index) {
        switch (exhibitionType){
            case 2:
                ApiManager.getCall(ApiManager.getInstance().create(YFApi.class)
                        .searchacting(index,activityid,vu.getEditText().getText().toString()))
                        .useCache(false)
                        .enqueue(new YFListHttpCallBack(getVu()) {
                            @Override
                            public void onSuccess(ApiBean mApiBean) {
                                super.onSuccess(mApiBean);
                                Allprojectsinfolist infolist;
                                if (!TextUtils.isEmpty(mApiBean.getData())) {
                                    infolist = JSON.parseObject(mApiBean.getData(), Allprojectsinfolist.class);
                                    if (infolist != null && infolist.getProjectList().size() > 0) {
                                        list.addAll(infolist.getProjectList());
                                        vu.getRecyclerView().getAdapter().notifyDataSetChanged();
                                    } else {
                                        vu.getRecyclerView().getPageManager().setPageState(PageInfo.PAGE_STATE_NO_MORE);
                                    }
                                }
                            }
                        });
                break;
            case 3:
                ApiManager.getCall(ApiManager.getInstance().create(YFApi.class)
                        .searchactend(index,activityid,vu.getEditText().getText().toString()))
                        .useCache(false)
                        .enqueue(new YFListHttpCallBack(getVu()) {
                            @Override
                            public void onSuccess(ApiBean mApiBean) {
                                super.onSuccess(mApiBean);
                                Allprojectsinfolist infolist;
                                if (!TextUtils.isEmpty(mApiBean.getData())) {
                                    infolist = JSON.parseObject(mApiBean.getData(), Allprojectsinfolist.class);
                                    if (infolist != null && infolist.getProjectList().size() > 0) {
                                        list.addAll(infolist.getProjectList());
                                        vu.getRecyclerView().getAdapter().notifyDataSetChanged();
                                    } else {
                                        vu.getRecyclerView().getPageManager().setPageState(PageInfo.PAGE_STATE_NO_MORE);
                                    }
                                }
                            }
                        });
                break;
        }
    }


    @Override
    public void onRefresh() {
    }

    @Override
    public void search(String searchKey) {
        switch (exhibitionType){
            case 2:
                ApiManager.getCall(ApiManager.getInstance().create(YFApi.class)
                        .searchacting(getVu().getRecyclerView().getPageManager().getCurrentIndex(),activityid,searchKey))
                        .useCache(false)
                        .enqueue(new YFListHttpCallBack(getVu()) {
                            @Override
                            public void onSuccess(ApiBean mApiBean) {
                                super.onSuccess(mApiBean);
                                Allprojectsinfolist infolist;
                                if (!TextUtils.isEmpty(mApiBean.getData())) {
                                    infolist = JSON.parseObject(mApiBean.getData(), Allprojectsinfolist.class);
                                    if (infolist != null && infolist.getProjectList().size() > 0) {
                                        list.clear();
                                        list.addAll(infolist.getProjectList());
                                        vu.getRecyclerView().getAdapter().notifyDataSetChanged();
                                    } else {
                                        list.clear();
                                        vu.getRecyclerView().getAdapter().notifyDataSetChanged();
                                        vu.setStateEmpty();
                                        vu.getRecyclerView().getPageManager().setPageState(PageInfo.PAGE_STATE_NO_MORE);
                                    }
                                } else {
                                    vu.setStateEmpty();
                                }
                            }
                        });
                break;
            case 3:
                ApiManager.getCall(ApiManager.getInstance().create(YFApi.class)
                        .searchactend(getVu().getRecyclerView().getPageManager().getCurrentIndex(),activityid,searchKey))
                        .useCache(false)
                        .enqueue(new YFListHttpCallBack(getVu()) {
                            @Override
                            public void onSuccess(ApiBean mApiBean) {
                                super.onSuccess(mApiBean);
                                Allprojectsinfolist infolist;
                                if (!TextUtils.isEmpty(mApiBean.getData())) {
                                    infolist = JSON.parseObject(mApiBean.getData(), Allprojectsinfolist.class);
                                    list.clear();
                                    if (infolist != null && infolist.getProjectList().size() > 0) {
                                        list.addAll(infolist.getProjectList());
                                    } else {
                                        vu.setStateEmpty();
                                        vu.getRecyclerView().getPageManager().setPageState(PageInfo.PAGE_STATE_NO_MORE);
                                    }
                                    vu.getRecyclerView().getAdapter().notifyDataSetChanged();
                                } else {
                                    vu.setStateEmpty();
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        getVu().getRecyclerView().getPageManager().resetIndex();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            //先隐藏键盘
            ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            search(vu.getEditText().getText().toString());
        }
        return false;
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
                if (choiceinfo != null) {
                    choiceinfo.setVotes(choiceinfo.getVotes() + voteNum);
                    getVu().getRecyclerView().getAdapter().notifyDataSetChanged();
                }
                break;
        }
    }
}
