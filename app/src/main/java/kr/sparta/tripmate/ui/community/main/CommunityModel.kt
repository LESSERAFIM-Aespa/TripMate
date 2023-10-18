package kr.sparta.tripmate.ui.community.main

data class CommunityModel(
    val id: Int, // 게시글 id
    val thumbnail: String?, // 이미지
    val title: String?, // 제목
    val profileNickname: String?, // 프로필 닉네임
    val profileThumbnail: String?, // 프로필 이미지
    val views: String?, // 조회수
    val likes: String?, // 좋아요 수
){

}

