package com.example.awsvmsguild.homeActivity



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.amazonaws.mobile.auth.core.signin.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.kotlin.core.Amplify
import com.example.awsvmsguild.R
import com.example.awsvmsguild.UploadActivity
import com.example.awsvmsguild.VideoPlayer
import com.example.awsvmsguild.data.VideoContent
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.*


class HomeActivity : AppCompatActivity() {
    private val model by viewModels<HomeViewModel>()
    private val data = ArrayList<VideoContent>()
    private var userId: MutableLiveData<String> = MutableLiveData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        GlobalScope.launch(Dispatchers.IO) {
            getUserSub()
        }

        with(recyclerView) {
            adapter = ThumbnailAdapter(data) {
                val intent = Intent(this@HomeActivity, VideoPlayer::class.java)
                intent.putExtra("vdocontent", it)
                startActivity(intent)
            }
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
        }
        observerData()
        observerView()
        tv_signout.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                onClickSigningOut()
            }
        }

        fab.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
        sl_recycler.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.IO) {
                getUserSub()
            }
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
        model.creatorvideo.observe(this) {
            data.clear()
            data.addAll(it)
            recyclerView.adapter?.notifyDataSetChanged()
        }

        userId.observe(this) {
            Log.d("userid", it)
            model.getVideo(it)
        }
    }
    private fun observerView() {
        model.loadingState.observe(this) {
            sl_recycler.isRefreshing = it
        }
    }

    private suspend fun getUserSub() {
        try {
            val session = Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession
            val id = session.userSub
            if (id.type == AuthSessionResult.Type.SUCCESS) {
                Log.i("sign in-id", "IdentityId: ${id.value}")
            } else if (id.type == AuthSessionResult.Type.FAILURE) {
                Log.i("AuthQuickStart", "IdentityId not present: ${id.error}")
            }
            userId.postValue(id.value.toString())

        } catch (error: AuthException) {
            Log.e("AuthQuickStart", "Failed to fetch session", error)
        }
    }
}