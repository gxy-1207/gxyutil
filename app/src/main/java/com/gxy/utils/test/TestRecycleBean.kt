package com.gxy.utils.test

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class TestRecycleBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: List<TestRecycleData>,
    @SerializedName("referer")
    val referer: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("status")
    val status: String
)

@Keep
data class TestRecycleData(
    @SerializedName("cid")
    val cid: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("lid")
    val lid: String,
    @SerializedName("picurl")
    val picurl: String,
    @SerializedName("sum")
    val sum: String,
    @SerializedName("uname")
    val uname: String
)