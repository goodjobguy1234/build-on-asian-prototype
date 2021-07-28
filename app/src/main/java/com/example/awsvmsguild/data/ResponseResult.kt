package com.example.awsvmsguild.data

data class ResponseResult(
    val statusCode: String = "",
    val body: ArrayList<ResultContent> = ArrayList()
)
