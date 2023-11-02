package kr.sparta.tripmate.domain.repository.search

import kr.sparta.tripmate.domain.model.search.SearchBlogServerDataEntity

/**
 * 작성자: 서정한
 * 내용: 블로그 검색 Repository
 * */
interface SearchRepository {
    suspend fun getSearchBlog(
        query: String,
        sort: String,
        display: Int,
    ): SearchBlogServerDataEntity
}