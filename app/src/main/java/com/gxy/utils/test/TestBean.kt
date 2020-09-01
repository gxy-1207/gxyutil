package com.gxy.utils.test

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class TestBean(
    @SerializedName("bay_type")
    val bayType: Int,
    @SerializedName("find")
    val find: Find,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("referer")
    val referer: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("status")
    val status: Int
)

@Keep
data class Find(
    @SerializedName("buy_img")
    val buyImg: String,
    @SerializedName("cost")
    val cost: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("descript")
    val descript: String,
    @SerializedName("doc_url")
    val docUrl: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("order_count")
    val orderCount: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("title")
    val title: String
)