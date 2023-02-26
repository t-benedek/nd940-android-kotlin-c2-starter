package com.udacity.asteroidradar.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageDay (
    val media_type: String,
    val url: String,
    val title: String
): Parcelable
