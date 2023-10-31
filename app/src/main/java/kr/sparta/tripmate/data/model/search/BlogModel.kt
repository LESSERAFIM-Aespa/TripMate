package kr.sparta.tripmate.data.model.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 작성자: 서정한
 * 내용: 블로그 검색 Model
 * */
@Parcelize
data class BlogModel(
    val title: String,
    val url: String,
    val description: String,
    val bloggername: String,
    val bloggerlink: String,
    val postdate: String,
    var isLike: Boolean = false
) : Parcelable {
    constructor() : this("", "", "", "", "", "", false)
}