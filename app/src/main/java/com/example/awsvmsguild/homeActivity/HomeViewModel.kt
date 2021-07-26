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

    fun getVideo(userId: String) {
        repo.fetchVideo(userId).observeForever {
                learnerVideo.value = it.body
            }
    }

}