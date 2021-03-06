package com.yushi.leke.fragment.ucenter;

import android.widget.TextView;

import com.yufan.library.base.Pr;
import com.yufan.library.base.Vu;

/**
 * Created by mengfantao on 18/8/2.
 */

public interface UCenterContract {
    interface IView extends Vu {
        void hasUnreadMsg(boolean hasUnreadMsg);

        void updateMyInfo(MyProfileInfo myProfileInfo, MyBaseInfo myBaseInfo);

        void refreshComplete();

        void updatcount(int count);
    }

    interface Presenter extends Pr {
        void startPlayer();

        void openMyWallet();

        void openPersonalpage();

        void share();

        void openSettingPage();

        void openBrowserPage(String key);

        void toRefresh();

        void openMessagePage();
    }
}
