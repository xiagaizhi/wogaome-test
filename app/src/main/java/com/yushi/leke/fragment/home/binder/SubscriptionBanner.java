package com.yushi.leke.fragment.home.binder;

import com.yushi.leke.fragment.home.bean.BannerItemInfo;

import java.util.List;

/**
 * Created by mengfantao on 18/8/30.
 */

public class SubscriptionBanner {
    private List<BannerItemInfo> bannerItemInfos;

    public List<BannerItemInfo> getBannerItemInfos() {
        return bannerItemInfos;
    }

    public void setBannerItemInfos(List<BannerItemInfo> bannerItemInfos) {
        this.bannerItemInfos = bannerItemInfos;
    }
}
