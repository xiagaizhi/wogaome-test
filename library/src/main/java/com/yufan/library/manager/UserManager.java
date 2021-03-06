package com.yufan.library.manager;

import android.text.TextUtils;


/**
 * Created by mengfantao on 18/4/3.
 */

public class UserManager {
    private String token;
    private String uid;
    private String qiNiuToken;
    private String latitude;
    private String longitude;

    public String getUid() {
        if(TextUtils.isEmpty(uid)){
            uid= SPManager.getInstance().getString("uid","");
        }
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
        SPManager.getInstance().saveValue("uid",uid);
    }

    private static UserManager instance=new UserManager();
    private UserManager(){

    }
    public static UserManager getInstance(){
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
        SPManager.getInstance().saveValue("token",token);
    }


    public String getToken() {
        if(TextUtils.isEmpty(token)){
            token= SPManager.getInstance().getString("token","");
        }
        return token;
    }





    public String getQiNiuToken() {
        return qiNiuToken;
    }

    public void setQiNiuToken(String qiNiuToken) {
        this.qiNiuToken = qiNiuToken;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
