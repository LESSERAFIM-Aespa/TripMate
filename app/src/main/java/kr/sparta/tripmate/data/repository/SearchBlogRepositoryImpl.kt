package kr.sparta.tripmate.data.repository

import kr.sparta.tripmate.data.datasource.remote.search.SearchBlogRemoteDataSource
import kr.sparta.tripmate.domain.model.search.ScrapServerDataEntity
import kr.sparta.tripmate.domain.model.search.toEntity
import kr.sparta.tripmate.domain.repository.search.SearchBlogRepository

class SearchBlogRepositoryImpl(
    private val remoteDataSource: SearchBlogRemoteDataSource
) : SearchBlogRepository {
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

