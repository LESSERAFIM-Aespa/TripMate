package kr.sparta.tripmate.data.model.search

import com.google.gson.annotations.SerializedName

data class SearchBlogServerData(
    @SerializedName("lastBuildDate") val lastBuildDate: String?,
    @SerializedName("total") val total: Int?,
    @SerializedName("start") val start: Int?,
    @SerializedName("display") val display: Int?,
    @SerializedName("items") val items: ArrayList<SearchBlogModel>?
)