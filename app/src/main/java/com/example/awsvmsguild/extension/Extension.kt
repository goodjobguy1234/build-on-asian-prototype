package com.example.awsvmsguild.extension

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.loadingDialog(@LayoutRes layout:Int, title: String, message: String): AlertDialog {
    val view = layoutInflater.inflate(layout, null)
    val loader = AlertDialog.Builder(this).apply {
        setView(view)
        setCancelable(false)
    }.create()
    return loader
}

fun AppCompatActivity.snackBarShow() {

}

fun AppCompatActivity.toastShow() {

}