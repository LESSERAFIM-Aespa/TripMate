package kr.sparta.tripmate.domain.repository

import kr.sparta.tripmate.domain.model.ScrapServerDataEntity

interface ScrapRepository {
    suspend fun getSearchBlog(
        query: String,
        sort: String,
        display: Int,
    ): ScrapServerDataEntity
}