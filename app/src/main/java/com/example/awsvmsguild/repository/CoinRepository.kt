package com.example.awsvmsguild.repository

import androidx.lifecycle.LiveData
import com.example.awsvmsguild.data.ResponseCoin
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CoinRepository {

    fun fetchCoin(userId: String): LiveData<ResponseCoin> {
        return object : LiveData<ResponseCoin>() {
            init {
                GlobalScope.launch(Dispatchers.IO) {
                    "https://56lh56t27g.execute-api.us-east-2.amazonaws.com/getUserCoin/getusercoin?userId=${userId}".httpGet()
                        .responseObject<ResponseCoin> { request, response, result ->
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