package com.example.awsvmsguild


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.MediaController

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobile.auth.core.signin.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.StorageException
import com.example.awsvmsguild.extension.loadingDialog
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.*


class HomeActivity : AppCompatActivity() {
    lateinit var loadingDialog: AlertDialog
    private var vdoUri: Uri? = null
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                setupVideoPlayer(it)
                vdoUri = it
            }
        }

    private val startRecorder =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Uri? = (result.data as Intent).data
                data?.let {
                    setupVideoPlayer(it)
                    vdoUri = it
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        loadingDialog = loadingDialog(R.layout.view_loading_dialog, "Uploading Video", "Loading")

        upload_btn.visibility = View.VISIBLE
        listOf<Button>(confirm_btn, cancel_btn).forEach { view ->
            view.visibility = View.GONE
        }

        tv_signout.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                onClickSigningOut()
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

    fun onUploadClicked(view: View) {
        val options = arrayOf<CharSequence>("From Camera", "From Gallery", "Cancel")

        AlertDialog.Builder(this).apply {
            setTitle("Uploading Video")
            setItems(options) { dialog, which ->
                when (options[which]) {

                    /*when choose the this option it will ask system to open the camera */
                    "From Camera" -> {
                        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                        startRecorder.launch(intent)
                    }

                    /*when choose the this option it will ask system to open the gallery */
                    "From Gallery" -> {
                        resultLauncher.launch("video/mp4")
                    }

                    "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            show()
        }

    }

    private fun setupVideoPlayer(uri: Uri) {
        with(videoView) {
            setVideoURI(uri)
            val mediaController = MediaController(this@HomeActivity)
            setMediaController(mediaController)
            mediaController.setAnchorView(this)
        }

        upload_btn.visibility = View.GONE
        listOf<Button>(confirm_btn, cancel_btn).forEach { view ->
            view.visibility = View.VISIBLE
        }
    }

    private suspend fun uploadVideo(uri: Uri) {
        GlobalScope.launch(Dispatchers.Main) {
            loadingDialog.show()
        }
        val session = Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession
        val id = session.identityId
        if (id.type == AuthSessionResult.Type.SUCCESS) {
            Log.i("AuthQuickStart", "IdentityId: ${id.value}")
            val stream = contentResolver.openInputStream(uri)
            val upload = Amplify.Storage.uploadInputStream("${id.value}:someExtra.mp4", stream!!)
            try {
                val result = upload.result()
                Log.i("MyAmplifyApp", "Successfully uploaded: ${result.key}.")
            } catch (error: StorageException) {
                Log.e("MyAmplifyApp", "Upload failed")
            }

        } else if (id.type == AuthSessionResult.Type.FAILURE) {
            Log.i("AuthQuickStart", "IdentityId not present: ${id.error}")
        }
    }

    fun onCancelClicked(view: View) {
        upload_btn.visibility = View.VISIBLE
        listOf<Button>(confirm_btn, cancel_btn).forEach { view ->
            view.visibility = View.GONE
        }
    }

    fun onConfirmClicked(view: View) {
        vdoUri?.let { uri ->
            GlobalScope.launch(Dispatchers.IO) {
                uploadVideo(uri)

                GlobalScope.launch(Dispatchers.Main) {
                    loadingDialog.hide()
                    upload_btn.visibility = View.VISIBLE
                    listOf<Button>(confirm_btn, cancel_btn).forEach { view ->
                        view.visibility = View.GONE
                    }
                }
            }
        }
    }
}