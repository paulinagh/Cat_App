package com.cat.core.repository

import com.cat.core.NetworkBoundResource
import com.cat.core.RemoteDataSource
import com.cat.core.RemoteNetworkBoundResource
import com.cat.core.Resource
import com.cat.core.domain.Cat
import com.cat.core.domain.CatDetail
import com.cat.core.local.LocalDataSource
import com.cat.core.remote.APIResponse
import com.cat.core.remote.CatResponse
import com.cat.core.remote.ListCatResponse
import com.cat.core.utils.AppExecutors
import com.cat.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CatRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : ICatRepository {
    override fun getAllCat(): Flow<Resource<List<Cat>>> =
        object : NetworkBoundResource<List<Cat>, List<ListCatResponse>>() {
            override fun loadFromDB(): Flow<List<Cat>> {
                return localDataSource.getAllCat().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Cat>?): Boolean =
                //data == null || data.isEmpty()
                true // ganti dengan true jika ingin selalu mengambil data dari internet

            override suspend fun createCall(): Flow<APIResponse<List<ListCatResponse>>> =
                remoteDataSource.getAllCat()

            override suspend fun saveCallResult(data: List<ListCatResponse>) {
                val catList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertCat(catList)
            }
        }.asFlow()

    override fun getAllCat(id: String): Flow<List<Cat>> {
        return localDataSource.getAllCat(id).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun getCat(id: String): Flow<Resource<CatDetail>> =
        object : RemoteNetworkBoundResource<CatDetail, CatResponse>() {
            override suspend fun createCall(): Flow<APIResponse<CatResponse>> =
                remoteDataSource.getCat(id)

            override suspend fun saveCallResult(data: CatResponse): Flow<CatDetail> =
                remoteDataSource.getCat(id).map { DataMapper.mapResponseToCatDetail(data) }

        }.asFlow()

    override fun getFavoriteCat(): Flow<List<Cat>> {
        return localDataSource.getFavoriteCat().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavoriteCat(cat: Cat, state: Boolean) {
        val catEntity = DataMapper.mapDomainToEntity(cat)
        appExecutors.diskIO().execute { localDataSource.setFavoriteCat(catEntity, state) }
    }
}