package kr.sparta.tripmate.domain.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kr.sparta.tripmate.util.ScrapInterface

/**
 * 작성자: 서정한
 * 내용: 블로그 검색결과 Entity
 * */
@Parcelize
data class SearchBlogEntity(
    val title: String?,
    val link: String?,
    val description: String?,
    val bloggername: String?,
    val bloggerlink: String?,
    val postdate: String?,
    val isLike: Boolean,
): Parcelable, ScrapInterface {
    constructor() :this (null, null, null, null, null, null, false)
}
