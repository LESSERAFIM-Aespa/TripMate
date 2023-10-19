package kr.sparta.tripmate.domain.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 작성자: 서정한
 * 내용: Firebase RDB의 scrapData를 가져와 사용할 Entity
 * */
@Parcelize
data class ScrapEntity(
    val title: String,
    val url: String,
    val description: String,
    val bloggername: String,
    val bloggerlink: String,
    val postdate: String,
    var isLike: Boolean = false
):Parcelable{
    constructor() : this("", "", "", "", "", "", false)
}
