package com.yufan.library.api.remote;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mengfantao on 18/2/27.
 */
public interface YFApi {

    @FormUrlEncoded
    @POST("/app/{partUrl}/{function}")
    Call<ResponseBody>
    postData(
            @Path("partUrl") String partUrl,
            @Path("function") String function,
            @Field("arg1") String arg1,
            @Field("arg2") String arg2
    );


    @FormUrlEncoded
    @POST("/app/{interfVersion}/listTreatureBox")
    Call<ResponseBody>
    listTreatureBox(
            @Path("interfVersion") String interfVersion,
            @Field("arg1") String goodsType
    );

    @FormUrlEncoded
    @POST("/app/{interfVersion}/payMethod")
    Call<ResponseBody>
    payMethod(
            @Path("interfVersion") String interfVersion,
            @Field("arg1") int isInternalPayIos,
            @Field("arg2") int goodsType,
            @Field("arg3") String goodsId
    );


    @FormUrlEncoded
    @POST("/app/{interfVersion}/{function}")
    Call<ResponseBody>
    toPay(
            @Path("interfVersion") String interfVersion,
            @Path("function") String function,
            @Field("arg1") String orderTitle,
            @Field("arg2") String orderPrice,
            @Field("arg3") String payPrice,
            @Field("arg4") String goodsId,
            @Field("arg5") String payApiId,
            @Field("arg6") String ioType

    );


}
