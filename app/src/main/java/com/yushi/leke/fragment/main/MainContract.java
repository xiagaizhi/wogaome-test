package com.yushi.leke.fragment.main;

import com.yufan.library.base.Pr;
import com.yufan.library.base.Vu;

/**
 * Created by mengfantao on 18/8/2.
 */

public interface MainContract {
    interface IView extends Vu {

        void hasUnreadMsg(boolean hasUnreadMsg);

    }

    interface Presenter extends Pr {
        void switchTab(int formPositon,int toPosition);
    }
}