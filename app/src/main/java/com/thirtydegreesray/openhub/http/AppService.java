/*
 *    Copyright 2017 ThirtyDegreesRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.http;

import android.support.annotation.NonNull;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * apps请求service
 * Created on 2016/10/19.
 *
 * @author ThirtyDegreesRay
 */

public interface AppService {

    @NonNull
    @POST("main/debug")
    Observable<ResponseBody> loginDebug(
            @Query("usr") String user,
            @Query("pwd") String pwd
    );

    /**
     * 日程事项查询
     *
     * @param userId    用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间，不包含当天的日程
     * @return
     */
    @NonNull
    @GET("schedule/querySchedule")
    Observable<ResponseBody> getScheduleEvent(
            @Query("uuid") String userId,
            @Query("startTime") String startTime,
            @Query("endTime") String endTime
    );

    /**
     * 获取组织结构，数据缓存时间600s
     *
     * @return
     */
    @NonNull
    @GET("contact/getAllOz")
    @Headers("Cache-Control: public, max-age=600")
    Observable<ResponseBody> getAllOrg();


    /**
     * 添加日程
     * @param userId
     * @param userName
     * @param requestData
     * @return
     */
    @NonNull
    @POST("schedule/addSchedule")
    @FormUrlEncoded
    Observable<ResponseBody> addScheduleEvent(
            @Field("userId") String userId,
            @Field("userName") String userName,
            @Field("req") String requestData
    );

    /**
     * 更新日程
     * @param userId
     * @param userName
     * @param requestData
     * @return
     */
    @NonNull
    @POST("schedule/updateSchedule")
    @FormUrlEncoded
    Observable<ResponseBody> updateScheduleEvent(
            @Field("userId") String userId,
            @Field("userName") String userName,
            @Field("req") String requestData
    );

    /**
     * 删除日程
     * @param eventId
     * @return
     */
    @NonNull
    @GET("schedule/delSchedule")
    Observable<ResponseBody> deleteScheduleEvent(
            @Query("id") String eventId
    );

    /**
     * 根据日程id获取日程详情
     * @param eventId
     * @return
     */
    @NonNull
    @GET("schedule/queryDetail")
    Observable<ResponseBody> getScheduleEventById(
            @Query("id") String eventId
    );

    /**
     * 上传手写签名
     * @param img
     * @param imgId
     * @param fileName
     * @param unitId
     * @param entityId
     * @return
     */
    @NonNull
    @POST("signImg/tjqm")
    @FormUrlEncoded
    Observable<ResponseBody> submitSignature(
            @Field("img") String img,
            @Field("imgId") String imgId,
            @Field("fileName") String fileName,
            @Field("unitId") String unitId,
            @Field("entityId") String entityId
    );

}
