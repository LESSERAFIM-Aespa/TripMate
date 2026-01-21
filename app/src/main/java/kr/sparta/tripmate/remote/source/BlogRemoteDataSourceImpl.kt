package kr.sparta.tripmate.remote.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.sparta.tripmate.remote.SearchBlogApiService
import kr.sparta.tripmate.remote.model.search.ImageServerData
import kr.sparta.tripmate.remote.model.search.SearchBlogServerData
import javax.inject.Inject

/**
 * Search blog remote data source impl
 * 블로그 검색 Api 구현체
 * @property api
 * @constructor Create empty Search blog remote data source impl
 */
class BlogRemoteDataSourceImpl @Inject constructor(private val api: SearchBlogApiService): BlogRemoteDataSource {
    override suspend fun searchBlogs(query: String, sort: String, display: Int): Flow<SearchBlogServerData> {
        require(query.isNotBlank()) {
            "Query must not be empty"
        }

        require(display in 1 .. 100) {
            "Display must be between 1 and 100"
        }

        return flow { emit(api.searchBlogs(query, sort, display)) }
    }

    override suspend fun getImage(query: String, sort: String): Flow<ImageServerData> {
        require(query.isNotBlank()) {
            "Query must not be empty"
        }

        return flow { emit(api.getImage(query, sort)) }
    }
}
