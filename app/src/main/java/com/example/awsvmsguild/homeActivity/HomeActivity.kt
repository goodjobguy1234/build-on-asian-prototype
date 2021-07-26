package com.example.awsvmsguild.homeActivity



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.amazonaws.mobile.auth.core.signin.AuthException
import com.amplifyframework.kotlin.core.Amplify
import com.example.awsvmsguild.R
import com.example.awsvmsguild.UploadActivity
import com.example.awsvmsguild.VideoPlayer
import com.example.awsvmsguild.data.VideoContent
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.*


class HomeActivity : AppCompatActivity() {
    val model by viewModels<HomeViewModel>()
    val data = ArrayList<VideoContent>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        model.getVideo("841abbca-76d6-4ca2-b97c-34110886086e")
        with(recyclerView) {
            adapter = ThumbnailAdapter(data) {
                val intent = Intent(this@HomeActivity, VideoPlayer::class.java)
                intent.putExtra("vdocontent", it)
                startActivity(intent)
            }
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
        }
        observerData()
        tv_signout.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                onClickSigningOut()
            }
        }

        fab.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun onClickSigningOut() {
        try {
            Amplify.Auth.signOut()
            Log.i("AuthQuickstart", "Signed out successfully")
            GlobalScope.launch(Dispatchers.Main) {
                finish()
            }
        } catch (error: AuthException) {
            Log.e("AuthQuickstart", "Sign out failed", error)
        }
    }

    private fun observerData() {
        model.learnerVideo.observe(this) {
            data.clear()
            data.addAll(it)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }


}