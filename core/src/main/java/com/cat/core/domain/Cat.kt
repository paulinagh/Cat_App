package com.cat.core.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cat(
    val id: String,
    val url: String,
    val createdAt: String? = null,
    var isFavorite: Boolean
) : Parcelable
