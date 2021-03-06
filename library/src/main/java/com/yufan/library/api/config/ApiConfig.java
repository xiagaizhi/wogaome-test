package com.yufan.library.api.config;

/**
 * Created by mengfantao on 18/8/24.
 */

public class ApiConfig {

    private String[] domains = new String[]{"http://app.leke-dev.com/", "http://app.leke-dev.com/", "http://app.test.leke.com/", "http://app.dev.leke.com/", "http://yapi.youximao.cn/mock/29/"};//域名
    private String[] webDomains = new String[]{"http://web.leke.com/", "http://web.leke.com/", "http://web.test.leke.com/", "http://web.dev.leke.com/", "http://web.leke-dev.com/"};//h5主域名
    private int apiType;//环境
    private final String protocol = "http://alifile.leke.com/public/protocol.html";


    public ApiConfig(int apiType) {
        this.apiType = apiType;

    }

    public String getBaseUrl() {
        return domains[apiType];
    }

    public String getProtocol() {
        return protocol;
    }

    /**
     * LKC明细
     */
    public String getLkcDetail() {
        return webDomains[apiType] + "#/myWallet/LKCDetail";
    }

    /**
     * LKC说明
     */
    public String getLKCInstruction() {
        return webDomains[apiType] + "#/myWallet/LKCInstruction";
    }

    /**
     * 我的会员
     */
    public String getMineVip() {
        return webDomains[apiType] + "#/myLeaguer";
    }

    /**
     * 我的等级
     */
    public String getMyGrades() {
        return webDomains[apiType] + "#/myGrade";
    }

    /**
     * 开宝箱明细
     */
    public String getTreasureDetail() {
        return webDomains[apiType] + "#/myWallet/treasureDetail";
    }

    /**
     * 我的算力
     */
    public String getMyPower() {
        return webDomains[apiType] + "#/myPower";
    }

    /**
     * 我的路演
     */
    public String getMyRoadShow() {
        return webDomains[apiType] + "#/play/my";
    }

    /**
     * 我的投票
     */
    public String getMyVote() {
        return webDomains[apiType] + "#/myVote";
    }

    /**
     * 分享好友
     */
    public String getFriendShare() {
        return webDomains[apiType] + "#/inviteFriends";
    }

    /**
     * 我的邀请
     *
     * @return
     */
    public String getMyInvite() {
        return webDomains[apiType] + "#/inviteFriends/myInvite";
    }

    /**
     * 活动详情页 未开始／报名中
     *
     * @param activityId
     * @return
     */
    public String getExhibitionDetail(String activityId) {
        return webDomains[apiType] + "#/play/detail/" + activityId;
    }


    /**
     * @param type 1:代表通知消息 2:代表系统消息
     * @return
     */
    public String getMessage(int type) {
        return webDomains[apiType] + "#/my/message/" + type;
    }


    /**
     * 往期活动
     *
     * @return
     */
    public String getPastActivities() {
        return webDomains[apiType] + "#/play/before";
    }

    /**
     * 报名信息页面
     *
     * @return
     */
    public String getSingUp(String projectId) {
        return webDomains[apiType] + "#/play/join/" + projectId;
    }

    /**
     * 活动说明页
     *
     * @return
     */
    public String getActivityIntroduction(String activityid) {
        return webDomains[apiType] + "#/play/desc/" + activityid;
    }

}
