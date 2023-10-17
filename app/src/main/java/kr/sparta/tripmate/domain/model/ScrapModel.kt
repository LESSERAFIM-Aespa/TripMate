package kr.sparta.tripmate.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScrapModel(
        val title: String,
    val url: String,
    val description: String,
    val bloggername: String,
    val bloggerlink: String,
    val postdate: String,
    var isLike:Boolean = false
) : Parcelable{
    constructor() : this("", "", "", "", "", "", false)
}