package com.example.awsvmsguild.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler

@Parcelize
data class SignUPData(
    var email: String,
    var username: String,
    var password: String,
    @TypeParceler<UserInfo, UserInfoClassParceler> () var userInfo: UserInfo? = null
): Parcelable
