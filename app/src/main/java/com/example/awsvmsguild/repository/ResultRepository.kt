package com.example.awsvmsguild.repository

import androidx.lifecycle.LiveData
import com.example.awsvmsguild.data.ResponseResult
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResultRepository {

    fun fetchResult(userId: String) : LiveData<ResponseResult> {
        return object : LiveData<ResponseResult>() {
            init {
                GlobalScope.launch(Dispatchers.IO) {
                    "https://56lh56t27g.execute-api.us-east-2.amazonaws.com/userResult/result?userId=${userId}".httpGet()
                        .responseObject<ResponseResult> { request, response, result ->
                            when (response.statusCode) {
                                in 200..205 -> {
                                    GlobalScope.launch(Dispatchers.Main) {
                                        value = result.component1()
                                    }
                                }
                                else -> print("response Response result Error")
                            }

                        }
                }
            }
        }
    }
}