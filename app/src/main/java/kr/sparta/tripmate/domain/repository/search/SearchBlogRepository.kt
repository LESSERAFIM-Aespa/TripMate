package kr.sparta.tripmate.domain.repository.search

import kr.sparta.tripmate.domain.model.search.ScrapServerDataEntity

interface SearchBlogRepository {
    suspend fun getSearchBlog(
        query: String,
        sort: String,
        display: Int,
    ): ScrapServerDataEntity
}