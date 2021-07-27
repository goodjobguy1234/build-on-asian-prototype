package com.example.awsvmsguild.homeActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.awsvmsguild.data.VideoContent
import com.example.awsvmsguild.repository.VideoRepository
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    var repo = VideoRepository()
    var learnerVideo = MutableLiveData<ArrayList<VideoContent>>()
    var creatorvideo = MutableLiveData<ArrayList<VideoContent>>()
    var loadingState = MutableLiveData<Boolean>()

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

}