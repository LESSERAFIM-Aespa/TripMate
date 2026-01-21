package kr.sparta.tripmate.data.datasource.remote

import kr.sparta.tripmate.remote.model.search.ImageServerData
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageRemoteDataSource {
    @GET("search/image")
    suspend fun getImage(
        @Query("query") query : String,
        @Query("sort") sort: String
    ): ImageServerData
}
