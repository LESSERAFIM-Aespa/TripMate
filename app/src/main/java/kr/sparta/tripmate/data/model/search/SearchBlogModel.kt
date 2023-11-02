package kr.sparta.tripmate.data.model.search

import com.google.gson.annotations.SerializedName

/**
 * 작성자: 서정한
 * 내용: 블로그 검색 Model
 * */
data class SearchBlogModel(
    @SerializedName("title") val title: String?,
    @SerializedName("link") val link: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("bloggername") val bloggername: String?,
    @SerializedName("bloggerlink") val bloggerlink: String?,
    @SerializedName("postdate") val postdate: String?
) {
    constructor() :this (null, null, null, null, null, null)
}