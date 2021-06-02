package com.keqin.libservice.server.api;

import com.keqin.libservice.server.impl.bean.ServerResult;
import com.keqin.libservice.server.bean.item.InfoBean;
import com.keqin.server.ServerApi;
import com.keqin.server.ServerRequest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * 信息请求
 *
 * @author Created by gold on 2018/1/19 14:34
 */
@ServerApi
public interface BasicApi {

    @POST("basic/info")
    Observable<ServerResult<List<InfoBean>>> info(@Query("params") String params);

    @POST("basic/info")
    @ServerRequest(String.class)
    Observable<ServerResult<List<InfoBean>>> testPost(@Query("params") String params);

    @GET("basic/info")
    @ServerRequest(String.class)
    Observable<ServerResult<List<InfoBean>>> testGet(@Query("params") String params);

    @PUT("basic/info")
    @ServerRequest(String.class)
    Observable<ServerResult<List<InfoBean>>> testPut(@Query("params") String params);

    @DELETE("basic/info")
    @ServerRequest(String.class)
    Observable<ServerResult<List<InfoBean>>> testDelete(@Query("params") String params);
}
