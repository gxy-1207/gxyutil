package com.gxy.utils.apis

import com.gxy.utils.test.TestBean
import com.gxy.utils.test.TestRecycleBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TestApi {
    @POST("adviser/get_adviser_content")
    @FormUrlEncoded
    fun testActivity(
        @Field("uid") uid: String,
        @Field("id") id: String
    ): Observable<TestBean>

    //http://ceshi.yuntaifawu.com/api/index/get_lvshi_huida
    //lid=4351&p=1
    @POST("index/get_lvshi_huida")
    @FormUrlEncoded
    suspend fun testRecycleActivity(
        @Field("lid") lid: String,
        @Field("p") p: Int
    ): TestRecycleBean
}