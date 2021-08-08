package com.example.awsvmsguild.homeActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.awsvmsguild.data.CoinContent
import com.example.awsvmsguild.data.VideoContent
import com.example.awsvmsguild.repository.CoinRepository
import com.example.awsvmsguild.repository.VideoRepository

class HomeViewModel: ViewModel() {
    var repo = VideoRepository()
    var coinRepo = CoinRepository()
    var learnerVideo = MutableLiveData<ArrayList<VideoContent>>()
    var creatorvideo = MutableLiveData<ArrayList<VideoContent>>()
    var loadingState = MutableLiveData<Boolean>()
    var userCoin = MutableLiveData<CoinContent>()

    fun getVideo(userId: String) {
        loadingState.value = true
        repo.fetchVideo(userId).observeForever {
                loadingState.value = false
                creatorvideo.value = it.body
            }

    }

    fun getLearnerVideo() {
        loadingState.value = true
        repo.fetchLearnerVideo().observeForever {
            loadingState.value = false
            learnerVideo.value = it.body
        }
    }

    fun getTotalCoin(userId: String) {
        loadingState.value = true
        coinRepo.fetchCoin(userId).observeForever {
            loadingState.value = false
            userCoin.value = it.body[0]
        }
    }

}