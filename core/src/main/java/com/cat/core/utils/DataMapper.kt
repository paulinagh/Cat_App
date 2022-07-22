package com.cat.core.utils

import com.cat.core.domain.Cat
import com.cat.core.domain.CatDetail
import com.cat.core.local.entity.CatEntity
import com.cat.core.remote.CatResponse
import com.cat.core.remote.ListCatResponse

object DataMapper {
    fun mapResponsesToEntities(input: List<ListCatResponse>): List<CatEntity> {
        val tourismList = ArrayList<CatEntity>()
        input.map {
            val cat = CatEntity(
                id = it.id,
                url = it.url,
                isFavorite = false
            )
            tourismList.add(cat)
        }
        return tourismList
    }

    fun mapResponseToCatDetail(input: CatResponse): CatDetail =
        CatDetail(input.id, input.url, input.createdAt, input.originalFilename)

    fun mapEntitiesToDomain(input: List<CatEntity>): List<Cat> =
        input.map {
            Cat(
                id = it.id,
                url = it.url,
                isFavorite = it.isFavorite
            )
        }

    fun mapDomainToEntity(input: Cat) = CatEntity(
        id = input.id,
        url = input.url,
        isFavorite = false
    )
}