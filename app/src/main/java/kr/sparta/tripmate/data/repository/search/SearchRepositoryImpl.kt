package kr.sparta.tripmate.data.repository.search

import kr.sparta.tripmate.data.datasource.remote.search.SearchBlogRemoteDataSource
import kr.sparta.tripmate.domain.model.search.SearchBlogServerDataEntity
import kr.sparta.tripmate.domain.model.search.toEntity
import kr.sparta.tripmate.domain.repository.search.SearchRepository

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
    ): SearchBlogServerDataEntity = remoteDataSource.getScrap(
        query,
        sort,
        display
    ).toEntity()
}

