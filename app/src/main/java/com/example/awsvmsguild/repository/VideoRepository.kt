package com.example.awsvmsguild.repository


import android.util.Log
import androidx.lifecycle.LiveData
import com.example.awsvmsguild.data.ResponseVideo
import com.example.awsvmsguild.data.VideoContent
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VideoRepository {

    fun fetchVideo(userId: String): LiveData<ResponseVideo> {
        return object : LiveData<ResponseVideo>() {
            init {
                GlobalScope.launch(Dispatchers.IO) {
                    "https://56lh56t27g.execute-api.us-east-2.amazonaws.com/text/text?userId=${userId}".httpGet()
                        .responseObject<ResponseVideo> { request, response, result ->
                            when (response.statusCode) {
                                in 200..205 -> {
                                    GlobalScope.launch(Dispatchers.Main) {
                                        value = result.component1()
                                    }
                                }
                                else -> print("Error")
                            }
                        }
                }
            }
        }
    }

    fun fetchLearnerVideo(): LiveData<ResponseVideo> {
        return object : LiveData<ResponseVideo>() {
            init {
                GlobalScope.launch(Dispatchers.IO) {
                    "https://56lh56t27g.execute-api.us-east-2.amazonaws.com/videoToLearner/video".httpGet()
                        .responseObject<ResponseVideo> { request, response, result ->
                            when (response.statusCode) {
                                in 200..205 -> {
                                    GlobalScope.launch(Dispatchers.Main) {
                                        value = result.component1()
                                    }
                                }
                                else -> print("Error")
                            }
                        }
                }
            }
        }
    }
}