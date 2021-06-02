package com.keqin.libservice.server.api;

import com.keqin.libservice.server.impl.bean.ServerResult;
import com.keqin.server.ServerRequest;
import com.keqin.libservice.server.bean.item.ConfigBean;
import com.keqin.libservice.server.bean.request.ConfigRequest;
import com.keqin.libservice.server.bean.request.PhoneRequest;
import com.keqin.libservice.server.bean.request.VerifyCodeRequest;
import com.keqin.libservice.server.impl.bean.NullResult;
import com.keqin.server.ServerApi;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 主页请求
 *
 * @author Created by gold on 2018/3/12 13:22
 */
@ServerApi
public interface MainApi {

    @GET("v1/app/config")
    Observable<ServerResult<NullResult>> notRequest(@Query("params") String params);

    @GET("v1/app/config")
    @ServerRequest(ConfigRequest.class)
    Observable<ServerResult<ConfigBean>> config(@Query("params") String params);

    @POST("v1/sms/verifycode")
    @com.keqin.server.ServerRequest(VerifyCodeRequest.class)
    Observable<ServerResult<NullResult>> verifyCode(@Query("params") String params);

    @POST("v1/sms/phone")
    @ServerRequest(PhoneRequest.class)
    Observable<ServerResult<NullResult>> phone(@Query("params") String params);
}
