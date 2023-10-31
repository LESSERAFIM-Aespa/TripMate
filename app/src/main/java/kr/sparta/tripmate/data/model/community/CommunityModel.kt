package kr.sparta.tripmate.data.model.community

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 작성자: 서정한
 * 내용: 커뮤니티 게시판 Model
 * */
@Parcelize
data class CommunityModel(
    val id: String, // 게시글 id
    val title: String?, // 제목
    val content: String?, // 게시글 내용
    val profileNickname: String?, // 프로필 닉네임
    val profileThumbnail: String?, // 프로필 이미지
    var views: Int?, // 조회수
    var likes: Int?, // 좋아요 수
    val key : String?, // 게시판 Unique key
    val image: String?, // 이미지
    var likeUsers: List<String>, // 해당 게시글의 좋아요를 누른 유저 모음(uid)
    var scrapUsers: List<String>, // 해당 게시글을 스크랩한 유저 모음(uid)

    ) : Parcelable{
    // 매개 변수 없는 생성자 추가 : 파이어베이스는 불러올때 빈생성자를 받아서 이렇게 사용해야함
    constructor() : this("", null, null, null, null, null, null, null,null, listOf(), listOf())
}