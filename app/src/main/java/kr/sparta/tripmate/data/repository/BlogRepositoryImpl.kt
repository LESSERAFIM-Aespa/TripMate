package kr.sparta.tripmate.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.sparta.tripmate.domain.model.scrap.ImageServerDataEntity
import kr.sparta.tripmate.domain.model.scrap.toEntity
import kr.sparta.tripmate.domain.model.search.SearchBlogServerDataEntity
import kr.sparta.tripmate.domain.model.search.toEntity
import kr.sparta.tripmate.domain.repository.BlogRepository
import kr.sparta.tripmate.remote.source.BlogRemoteDataSource
import javax.inject.Inject

/**
 * Blog repository impl
 * 블로그 검색 Repository 구현체
 * @property source
 * @constructor Create empty Blog repository impl
 */
class BlogRepositoryImpl @Inject constructor(private val source: BlogRemoteDataSource) :
    BlogRepository {
    override suspend fun getSearchBlog(
        query: String,
        sort: String,
        display: Int,
    ): Flow<SearchBlogServerDataEntity> =
        source.searchBlogs(query, sort, display).map { it.toEntity() }

    override suspend fun getImage(query: String, sort: String): Flow<ImageServerDataEntity> =
        source.getImage(query, sort).map { it.toEntity() }
}