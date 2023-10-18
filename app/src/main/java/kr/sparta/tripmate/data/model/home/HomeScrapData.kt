package kr.sparta.tripmate.data.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class HomeScrapData(
    val type: Int,
    val title: String,
    val url: String,
    val description: String,
    val bloggername: String,
    val bloggerlink: String,
    val postdate: String,
    var isLike:Boolean = false
) : Parcelable{
    constructor() : this(0,"", "", "", "", "", "", false)
}
