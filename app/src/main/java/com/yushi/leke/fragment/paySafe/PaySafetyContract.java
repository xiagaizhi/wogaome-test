package com.yushi.leke.fragment.paySafe;

import com.yufan.library.base.Pr;
import com.yufan.library.base.Vu;

/**
 * Created by mengfantao on 18/8/2.
 */

public interface PaySafetyContract {
    interface IView extends Vu {
        void updatePage(int isHavePwd, String phoneNumber);
    }

    interface Presenter extends Pr {
        void forgetPwd();

        void setRechargePwd();
    }
}
