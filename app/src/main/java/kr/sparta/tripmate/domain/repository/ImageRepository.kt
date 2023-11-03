package kr.sparta.tripmate.domain.repository

import kr.sparta.tripmate.domain.model.scrap.ImageServerDataEntity

interface ImageRepository {
    suspend fun getImage(
        query : String,
        sort : String
    ) : ImageServerDataEntity
}