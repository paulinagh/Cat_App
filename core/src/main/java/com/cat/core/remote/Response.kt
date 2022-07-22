package com.cat.core.remote

import com.google.gson.annotations.SerializedName

data class ListCatResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("url")
    val url: String,

    //@field:SerializedName("results")
    //val results: List<CatDetail>
)

data class CatResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("original_filename")
    val originalFilename: String
)