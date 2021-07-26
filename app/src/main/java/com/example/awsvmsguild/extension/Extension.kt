package com.example.awsvmsguild.extension

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_video_player.*

fun AppCompatActivity.loadingDialog(@LayoutRes layout:Int, title: String, message: String): AlertDialog {
    val view = layoutInflater.inflate(layout, null)
    val loader = AlertDialog.Builder(this).apply {
        setView(view)
        setCancelable(false)
    }.create()
    return loader
}

fun View.snackBarShow(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun AppCompatActivity.toastShow() {

}