package com.yushi.leke.fragment.searcher.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yufan.library.Global;
import com.yufan.library.api.ApiBean;
import com.yufan.library.api.ApiManager;
import com.yufan.library.api.YFListHttpCallBack;
import com.yufan.library.base.BaseListFragment;
import com.yufan.library.inject.VuClass;
import com.yufan.library.inter.ICallBack;
import com.yufan.library.util.SoftInputUtil;
import com.yufan.library.widget.anim.AFVerticalAnimator;
import com.yushi.leke.UIHelper;
import com.yushi.leke.YFApi;
import com.yushi.leke.fragment.browser.BrowserBaseFragment;
import com.yushi.leke.fragment.exhibition.detail.ExhibitionDetailFragment;
import com.yushi.leke.fragment.searcher.SearchActionInfo;
import com.yushi.leke.fragment.searcher.SearchActionViewBinder;
import com.yushi.leke.fragment.searcher.SearchTabTitleViewBinder;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by mengfantao on 18/8/2.
 */
@VuClass(SearchActivityVu.class)
public class SearchActivityFragment extends BaseListFragment<SearchActivityContract.IView> implements SearchActivityContract.Presenter {
    private MultiTypeAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new MultiTypeAdapter();
        adapter.register(SearchActionInfo.class,new SearchActionViewBinder(new ICallBack() {
            @Override
            public void OnBackResult(Object... s) {
                SearchActionInfo info= (SearchActionInfo) s[0];
                if (info.getProcessStatus()==0||info.getProcessStatus()==1){
                    getRootFragment().start(UIHelper.creat(BrowserBaseFragment.class)
                            .put(Global.BUNDLE_KEY_BROWSER_URL, ApiManager.getInstance().
                                    getApiConfig()
                                    .getExhibitionDetail(info.getId()))
                            .build());
                }else {//原生详情页面
                    getRootFragment().start(UIHelper.creat(ExhibitionDetailFragment.class)
                            .put(Global.BUNDLE_KEY_EXHIBITION_TYE, info.getProcessStatus())
                            .put(Global.BUNDLE_KEY_ACTIVITYID, info.getId())
                            .build());
                }
            }
        }));
        adapter.register(String.class,new SearchTabTitleViewBinder());
        vu.getRecyclerView().setAdapter(adapter);
        adapter.setItems(list);
        vu.getRecyclerView().getAdapter().notifyDataSetChanged();
        Bundle bundle = getArguments();
        if (bundle != null) {
            vu.getEditText().setText(bundle.getString(Global.SEARCH_KEY));
            search(bundle.getString(Global.SEARCH_KEY));
        }
    }



    @Override
    public void onLoadMore(int index) {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class)
                .activitySearch(getVu().getEditText().getText().toString(),getVu().getRecyclerView().getPageManager().getCurrentIndex())).enqueue(new YFListHttpCallBack(vu) {
            @Override
            public void onSuccess(ApiBean mApiBean) {
                super.onSuccess(mApiBean);
                JSONObject jsonObject= JSON.parseObject(mApiBean.data);
                List<SearchActionInfo> actionInfos= JSON.parseArray(jsonObject.getString("list"),SearchActionInfo.class);
                list.addAll(actionInfos);
                vu.getRecyclerView().getAdapter().notifyDataSetChanged();
            }

        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SoftInputUtil.hideSoftInput(getActivity(),getView());
    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new AFVerticalAnimator();
    }

    @Override
    public void search(String searchKey) {
        ApiManager.getCall(ApiManager.getInstance().create(YFApi.class).activitySearch(searchKey,1)).enqueue(new YFListHttpCallBack(vu) {
            @Override
            public void onSuccess(ApiBean mApiBean) {
                super.onSuccess(mApiBean);
                JSONObject jsonObject= JSON.parseObject(mApiBean.data);
                List<SearchActionInfo> actionInfos= JSON.parseArray(jsonObject.getString("list"),SearchActionInfo.class);
                list.clear();
                list.add("活动");
                list.addAll(actionInfos);
                vu.getRecyclerView().getAdapter().notifyDataSetChanged();
            }


        });
        SoftInputUtil.hideSoftInput(getActivity(),getView());
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            //先隐藏键盘
            ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            search(vu.getEditText().getText().toString());
        }
        return false;
    }
}
