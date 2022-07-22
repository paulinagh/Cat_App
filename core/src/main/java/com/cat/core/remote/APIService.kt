package com.cat.core.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET("$IMAGES/search")
    suspend fun getCatList(): List<ListCatResponse>

    @GET("$IMAGES/{image_id}")
    suspend fun getCat(
        @Path("image_id") id: String
    ): CatResponse

    companion object {
        private const val IMAGES = "images"
    }
}