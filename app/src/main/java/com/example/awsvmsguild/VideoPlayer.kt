package com.example.awsvmsguild

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.awsvmsguild.data.VideoContent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayer : AppCompatActivity() {
    private var data: VideoContent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        data = intent?.getParcelableExtra<VideoContent>("vdocontent")
        var mode = intent?.getBooleanExtra("creatorMode", false)

        tv_describe.text = data?.text_result
        with(video_view) {
            setErrorTracker {
                Snackbar.make(video_view, "Uh oh, error playing!", Snackbar.LENGTH_INDEFINITE).show()
            }

            start(data?.url)
            setOnClickListener {
                if (isPlaying) pause()
                else play()
            }
            setShouldLoop(true)

        }

        mode?.let {
            if(it) fab.hide()
            else fab.show()
        }

        fab.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            intent.putExtra("creatorMode", mode)
            intent.putExtra("vdoid", data!!.id)
            startActivity(intent)
        }

    }

    override fun onPause() {
        super.onPause()
        video_view.release()
    }
    override fun onStop() {
        super.onStop()
        video_view.release()
    }
}