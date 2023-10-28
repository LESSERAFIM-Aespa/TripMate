package kr.sparta.tripmate.domain.model.community

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.sparta.tripmate.util.ScrapInterface

@Parcelize
data class CommunityEntity(
    val key: String?, // 각 게시판의 고유 id
    val title: String?, // 제목
    val description: String?, // 게시글 내용
    val userid: String?, // 글 작성자 id
    val userNickname: String?, // 프로필 닉네임
    val userThumbnail: String?, // 프로필 이미지
    val views: String?, // 조회수
    val likes: String?, // 좋아요 수
    val image: String?, // 이미지
    val isLike: Boolean = false, // 좋아요

) : Parcelable, ScrapInterface {
    // 매개 변수 없는 생성자 추가 : 파이어베이스는 불러올때 빈생성자를 받아서 이렇게 사용해야함
    constructor() : this(null, null, null, null, null, null, null, null, null, false)
}