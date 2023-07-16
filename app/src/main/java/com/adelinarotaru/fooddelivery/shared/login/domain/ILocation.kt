package com.adelinarotaru.fooddelivery.shared.login.domain

import android.os.Parcelable

interface ILocation : Parcelable {
    val latitude: String?
    val longitude: String?
}