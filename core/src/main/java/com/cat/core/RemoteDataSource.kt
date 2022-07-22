package com.cat.core

import android.util.Log
import com.cat.core.remote.APIResponse
import com.cat.core.remote.APIService
import com.cat.core.remote.CatResponse
import com.cat.core.remote.ListCatResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: APIService) {
    suspend fun getAllCat(): Flow<APIResponse<List<ListCatResponse>>> {
        //get data from remote api
        return flow {
            try {
                val response = apiService.getCatList()
                if (response.isNotEmpty()) {
                    emit(APIResponse.Success(response))
                } else {
                    emit(APIResponse.Empty)
                }
            } catch (e: Exception) {
                emit(APIResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCat(id: String): Flow<APIResponse<CatResponse>> {
        //get data from remote api
        return flow {
            try {
                val response = apiService.getCat(id)
                if (response.id.isNotEmpty()) {
                    emit(APIResponse.Success(response))
                } else {
                    emit(APIResponse.Empty)
                }
            } catch (e: Exception) {
                emit(APIResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}