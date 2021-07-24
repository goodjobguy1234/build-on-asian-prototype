package com.example.awsvmsguild.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class UserInfo(
    var firstname: String,
    var lastname: String,
    var gender: String,
    var birthdate: String
): Parcelable

object UserInfoClassParceler: Parceler<UserInfo> {
    override fun create(parcel: Parcel): UserInfo {
        return UserInfo(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
        )
    }

    override fun UserInfo.write(parcel: Parcel, flags: Int) {
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeString(gender)
        parcel.writeString(birthdate)
    }

}
