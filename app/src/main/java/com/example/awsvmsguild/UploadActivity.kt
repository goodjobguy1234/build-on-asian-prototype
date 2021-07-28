package com.example.awsvmsguild

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.StorageException
import com.example.awsvmsguild.extension.loadingDialog
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class UploadActivity : AppCompatActivity() {
    lateinit var loadingDialog: AlertDialog
    private var vdoUri: Uri? = null
    private var mode: Boolean = false
    private var vdoId: Int = -1
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
        setContentView(R.layout.activity_upload)

        mode = intent.getBooleanExtra("creatorMode", false)
        vdoId = intent.getIntExtra("vdoid", -1)
        Log.d("uploadMode", mode.toString())
        Log.d("videoId", vdoId.toString())

        loadingDialog = loadingDialog(R.layout.view_loading_dialog, "Uploading Video", "Loading")

        upload_btn.visibility = View.VISIBLE
        listOf<Button>(confirm_btn, cancel_btn).forEach { view ->
            view.visibility = View.INVISIBLE
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
            val mediaController = MediaController(this@UploadActivity)
            setMediaController(mediaController)
            mediaController.setAnchorView(this)
        }

        upload_btn.visibility = View.INVISIBLE
        listOf<Button>(confirm_btn, cancel_btn).forEach { view ->
            view.visibility = View.VISIBLE
        }
    }

    private suspend fun uploadVideo(uri: Uri) {
        GlobalScope.launch(Dispatchers.Main) {
            loadingDialog.show()
        }
        val session = Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession
        val id = session.userSub
        if (id.type == AuthSessionResult.Type.SUCCESS) {

            Log.i("AuthQuickStart", "IdentityId: ${id.value}")
            val stream = contentResolver.openInputStream(uri)
            val timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss.SSSSSS").withZone(
                ZoneOffset.UTC
            ).format(Instant.now())

            var key =  if (mode || vdoId == -1) "creator/${id.value!!}-${timestamp}.mp4"
                        else "learner/${vdoId}-${id.value!!}-${timestamp}.mp4"

            val upload = Amplify.Storage.uploadInputStream(key, stream!!)

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
            view.visibility = View.INVISIBLE
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
                        view.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        loadingDialog.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("savedMode", mode)
        outState.putInt("saveVdoId", vdoId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        vdoId = savedInstanceState.getInt("saveVdoId")
        mode = savedInstanceState.getBoolean("savedMode")
        Log.d("resotoring", "${vdoId} mode: ${mode}")
    }
}