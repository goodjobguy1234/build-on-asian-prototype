package com.example.awsvmsguild.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoContent(
    val id: Int = -1,
    val url: String = "",
    val text_result: String = "Unknown"
): Parcelable
