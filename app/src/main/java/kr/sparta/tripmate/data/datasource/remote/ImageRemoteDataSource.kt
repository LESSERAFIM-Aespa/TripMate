package kr.sparta.tripmate.data.datasource.remote

import kr.sparta.tripmate.api.Constants
import kr.sparta.tripmate.remote.model.search.ImageServerData
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ImageRemoteDataSource {
    @Headers(
        "X-Naver-Client-Id: ${Constants.client_id}",
        "X-Naver-Client-Secret: ${Constants.client_secret}"
    )
    @GET("search/image")
    suspend fun getImage(
        @Query("query") query : String,
        @Query("sort") sort: String
    ): ImageServerData
}