package kr.sparta.tripmate.data.datasource.remote.search

import kr.sparta.tripmate.remote.model.search.SearchBlogServerData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 작성자: 박성수
 * 내용: 블로그 검색
 * */
interface SearchBlogRemoteDataSource {
    @GET("search/blog.json")
    suspend fun getScrap(
        @Query("query") query : String,
        @Query("sort") sort: String,
        @Query("display") display: Int,
    ): SearchBlogServerData
}
