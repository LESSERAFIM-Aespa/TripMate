package kr.sparta.tripmate.ui.community.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityModel(
    val id: String, // 게시글 id
    val thumbnail: String?, // 이미지
    val title: String?, // 제목
    val body: String?, // 게시글 내용
    val profileNickname: String?, // 프로필 닉네임
    val profileThumbnail: String?, // 프로필 이미지
    val views: String?, // 조회수
    val likes: String? // 좋아요 수
) : Parcelable{
    // 매개 변수 없는 생성자 추가 : 파이어베이스는 불러올때 빈생성자를 받아서 이렇게 사용해야함
    constructor() : this("", null, null, null, null, null, null, null)
}
