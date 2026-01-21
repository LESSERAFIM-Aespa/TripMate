package kr.sparta.tripmate.remote

import kr.sparta.tripmate.remote.model.search.ImageServerData
import kr.sparta.tripmate.remote.model.search.SearchBlogServerData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Search blog api service
 * 네이버 블로그 검색 Api
 * @constructor Create empty Search blog api service
 */
interface SearchBlogApiService {
    /**
     * Search blogs
     * 블로그 검색 결과를 반환합니다
     * @param query 검색어
     * @param sort sorting
     * @param display 화면에 보여줄 item 갯수
     * @return
     */
    @GET("search/blog.json")
    suspend fun searchBlogs(
        @Query("query") query : String,
        @Query("sort") sort: String = "sim",
        @Query("display") display: Int = 10,
    ): SearchBlogServerData

    /**
     * Get image
     * 이미지 검색 결과를 반환합니다.
     * @param query 검색어
     * @param sort sorting
     * @return
     */
    @GET("search/image")
    suspend fun getImage(
        @Query("query") query : String,
        @Query("sort") sort: String
    ): ImageServerData
}
