package kr.sparta.tripmate.data.model.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchBlogModel(
    val title: String,
    val url: String,
    val description: String,
    val bloggername: String,
    val bloggerlink: String,
    val postdate: String,
    val isLike: Boolean = false
) : Parcelable {
    constructor() : this("", "", "", "", "", "", false)
}