package kr.sparta.tripmate.domain.model.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.sparta.tripmate.util.ScrapInterface

/**
 * 작성자: 서정한
 * 내용: Firebase RDB의 scrapData를 가져와 사용할 Entity
 * */
@Parcelize
data class SearchBlogEntity(
    val title: String,      //게시글 제목
    val url: String,        //게시글 url
    val description: String,    //게시글 내용
    val bloggername: String,    //작성자 이름
    val bloggerlink: String,    //블로그 주소(사용안함)
    val postdate: String,       //작성 날짜
    val isLike: Boolean = false //좋아요 불리언값
) : Parcelable, ScrapInterface {
    constructor() : this("", "", "", "", "", "", false)

}
