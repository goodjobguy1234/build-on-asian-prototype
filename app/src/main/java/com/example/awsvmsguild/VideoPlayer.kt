package com.example.awsvmsguild

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.awsvmsguild.data.VideoContent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayer : AppCompatActivity() {
    lateinit var data: VideoContent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val data = intent.getParcelableExtra<VideoContent>("vdocontent")

        with(video_view) {
            setErrorTracker {
                Snackbar.make(video_view, "Uh oh, error playing!", Snackbar.LENGTH_INDEFINITE).show();
            }

            start(data?.url)
            setOnClickListener {
                if (isPlaying) pause()
                else play()
            }

        }

    }

    override fun onStop() {
        super.onStop()
        video_view.release()
    }
}