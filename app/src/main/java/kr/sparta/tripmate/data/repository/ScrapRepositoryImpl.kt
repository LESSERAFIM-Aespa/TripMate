package kr.sparta.tripmate.data.repository

import kr.sparta.tripmate.data.datasource.remote.ScrapRemoteDataSource
import kr.sparta.tripmate.data.model.ScrapServerData
import kr.sparta.tripmate.domain.model.ScrapServerDataEntity
import kr.sparta.tripmate.domain.model.toEntity
import kr.sparta.tripmate.domain.repository.ScrapRepository

class ScrapRepositoryImpl(
    private val remoteDataSource: ScrapRemoteDataSource
) : ScrapRepository {
    override suspend fun getSearchBlog(
        query: String,
        sort: String,
        display: Int,
    ): ScrapServerDataEntity = remoteDataSource.getScrap(
        query,
        sort,
        display
    ).toEntity()
}

