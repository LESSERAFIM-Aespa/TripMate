package kr.sparta.tripmate.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.domain.model.scrap.ImageServerDataEntity
import kr.sparta.tripmate.domain.model.search.SearchBlogServerDataEntity

/**
 * Blog repository
 * 블로그 검색 Repository
 * @constructor Create empty Blog repository
 */
interface BlogRepository {
    suspend fun getSearchBlog(
        query: String,
        sort: String,
        display: Int,
    ): Flow<SearchBlogServerDataEntity>

    suspend fun getImage(
        query : String,
        sort : String
    ) : Flow<ImageServerDataEntity>
}