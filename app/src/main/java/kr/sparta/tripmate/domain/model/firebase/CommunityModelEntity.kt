package kr.sparta.tripmate.domain.model.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class CommunityModelEntity (
    val id: String, // 게시글 id
    val thumbnail: String?, // 이미지
    val title: String?, // 제목
    val body: String?, // 게시글 내용
    val profileNickname: String?, // 프로필 닉네임
    val profileThumbnail: String?, // 프로필 이미지
    var views: String?, // 조회수
    var likes: String?, // 좋아요 수
    val key : String?,
    val addedImage: String?,
    var commuIsLike:Boolean = false,
    var boardIsLike:Boolean = false,

    ) : Parcelable{
    // 매개 변수 없는 생성자 추가 : 파이어베이스는 불러올때 빈생성자를 받아서 이렇게 사용해야함
    constructor() : this("", null, null, null, null, null, null, null, null,null)
}