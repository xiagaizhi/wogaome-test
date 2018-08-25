package com.yufan.library.pay;

import java.util.List;

/**
 * 作者：Created by zhanyangyang on 2018/8/25 11:04
 * 邮箱：zhanyangyang@hzyushi.cn
 */

public class PayWayList {
    private List<PayWay> payApi;
//    private int isInternalPayIos;//ios是否内购（0-第三方支付，1-ios内购）不用考虑
    private String goodsName;//商品名称
    private String goodsPrice;//商品单价
    private String goodsId;//商品ID

    public List<PayWay> getPayApi() {
        return payApi;
    }

    public void setPayApi(List<PayWay> payApi) {
        this.payApi = payApi;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
