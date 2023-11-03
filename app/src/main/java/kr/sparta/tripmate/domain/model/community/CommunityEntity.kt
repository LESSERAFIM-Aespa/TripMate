package kr.sparta.tripmate.domain.model.community

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.sparta.tripmate.util.ScrapInterface

@Parcelize
data class CommunityEntity(
    val id: String?, // 게시글 id
    val key: String?, // 게시판 Unique key
    val title: String?, // 제목
    val content: String?, // 게시글 내용
    val profileNickname: String?, // 프로필 닉네임
    val profileThumbnail: String?, // 프로필 이미지
    val views: Int?, // 조회수
    val likes: Int?, // 좋아요 수
    val image: String?, // 이미지
    val likeUsers: List<String>, // 좋아요 누른 유저목록
    val scrapUsers: List<String> // 스크랩한 유저목록
) : Parcelable, ScrapInterface {
    // 매개 변수 없는 생성자 추가 : 파이어베이스는 불러올때 빈생성자를 받아서 이렇게 사용해야함
    constructor() : this("", null, null, null, null, null, 0, 0, null, listOf(), listOf())
}