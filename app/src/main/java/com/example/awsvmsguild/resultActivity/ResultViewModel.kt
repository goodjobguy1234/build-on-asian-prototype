package com.example.awsvmsguild.resultActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.awsvmsguild.data.ResultContent
import com.example.awsvmsguild.data.VideoContent
import com.example.awsvmsguild.repository.ResultRepository

class ResultViewModel: ViewModel() {
    var repo = ResultRepository()
    var matchingResult = MutableLiveData<ArrayList<ResultContent>>()
    var loadingState = MutableLiveData<Boolean>()

    fun getResult(userId: String) {
        loadingState.value = true
        repo.fetchResult(userId).observeForever {
            loadingState.value = false
            matchingResult.value = it.body
        }
    }
}