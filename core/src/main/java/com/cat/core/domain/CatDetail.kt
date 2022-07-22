package com.cat.core.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CatDetail(
    val id: String,
    val url: String,
    val createdAt: String? = null,
    val originalFileName: String? = null
) : Parcelable