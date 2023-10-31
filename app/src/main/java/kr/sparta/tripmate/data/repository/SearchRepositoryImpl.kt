package kr.sparta.tripmate.data.repository

import kr.sparta.tripmate.data.datasource.remote.SearchBlogRemoteDataSource
import kr.sparta.tripmate.domain.model.scrap.ScrapServerDataEntity
import kr.sparta.tripmate.domain.model.scrap.toEntity
import kr.sparta.tripmate.domain.repository.SearchRepository

/**
 * 작성자: 서정한
 * 내용: 블로그 검색 Repository
 * */
class SearchRepositoryImpl(
    private val remoteDataSource: SearchBlogRemoteDataSource
) : SearchRepository {
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

