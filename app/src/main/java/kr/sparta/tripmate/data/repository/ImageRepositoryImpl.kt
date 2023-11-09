package kr.sparta.tripmate.data.repository

import kr.sparta.tripmate.data.datasource.remote.ImageRemoteDataSource
import kr.sparta.tripmate.domain.model.scrap.ImageServerDataEntity
import kr.sparta.tripmate.domain.model.scrap.toEntity
import kr.sparta.tripmate.domain.repository.ImageRepository

class ImageRepositoryImpl(private val remoteDataSource: ImageRemoteDataSource) : ImageRepository {
    override suspend fun getImage(query: String, sort: String): ImageServerDataEntity = remoteDataSource.getImage(query, sort).toEntity()
}