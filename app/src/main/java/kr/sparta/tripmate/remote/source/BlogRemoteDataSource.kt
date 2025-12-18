package kr.sparta.tripmate.remote.source

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.remote.model.search.ImageServerData
import kr.sparta.tripmate.remote.model.search.SearchBlogServerData

interface BlogRemoteDataSource {
    suspend fun searchBlogs(
        query: String,
        sort: String,
        display: Int,
    ): Flow<SearchBlogServerData>

    suspend fun getImage(
        query : String,
        sort: String
    ): Flow<ImageServerData>
}