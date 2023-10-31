package kr.sparta.tripmate.domain.repository

import kr.sparta.tripmate.domain.model.scrap.ScrapServerDataEntity

/**
 * 작성자: 서정한
 * 내용: 블로그 검색 Repository
 * */
interface SearchRepository {
    suspend fun getSearchBlog(
        query: String,
        sort: String,
        display: Int,
    ): ScrapServerDataEntity
}