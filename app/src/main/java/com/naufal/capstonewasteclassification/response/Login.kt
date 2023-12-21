package com.naufal.capstonewasteclassification.response

import com.google.gson.annotations.SerializedName

data class Login(
    @field:SerializedName("loginResult")
    val loginResult: Result,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
data class Result(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("token")
    val token: String
)
