package com.yushi.leke.fragment.ucenter;

import android.content.Intent;
import android.os.Bundle;

import com.yufan.library.base.BaseFragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.yufan.library.inject.VuClass;
import com.yufan.library.manager.SPManager;
import com.yufan.library.manager.UserManager;
import com.yushi.leke.UIHelper;
import com.yushi.leke.fragment.login.LoginFragment;
import com.yushi.leke.fragment.main.MainFragment;
import com.yushi.leke.fragment.musicplayer.MusicPlayerFragment;
import com.yushi.leke.fragment.wallet.MyWalletFragment;
import com.yushi.leke.activity.QuarantineActivity;
import com.yushi.leke.uamp.ui.MediaBrowserFragment;

/**
 * Created by mengfantao on 18/8/2.
 */
@VuClass(UCenterVu.class)
public class UCenterFragment extends BaseFragment<UCenterContract.IView> implements UCenterContract.Presenter {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void startPlayer() {
        Intent intent=new Intent(getContext(),QuarantineActivity.class);
        startActivity(intent);

    }

    @Override
    public void startPlayerList() {
        getRootFragment().start(UIHelper.creat(MediaBrowserFragment.class).build());
    }

    @Override
    public void logout() {
        UserManager.getInstance().setToken("");
        UserManager.getInstance().setUid("");
        getRootFragment().startWithPopTo(UIHelper.creat(LoginFragment.class).build(), MainFragment.class,true);
    }

    @Override
    public void openMyWallet() {
        getRootFragment().start(UIHelper.creat(MyWalletFragment.class).build());
    }
}
