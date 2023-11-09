package kr.sparta.tripmate.domain.model.community

import kr.sparta.tripmate.data.model.community.CommunityModel

fun CommunityModel.toEntity(): CommunityEntity = CommunityEntity(
    userid = userid,// 게시글 id
    title = title, // 제목
    content = content, // 게시글 내용
    profileNickname = profileNickname, // 프로필 닉네임
    profileThumbnail = profileThumbnail, // 프로필 이미지
    views = views, // 조회수
    likes = likes, // 좋아요 수
    key = key, // 게시판 Unique key
    image = image, // 이미지
    likeUsers= likeUsers, // 좋아요 누른 유저
    scrapUsers=scrapUsers // 스크랩한 유저목

)

fun CommunityEntity.toModel() = CommunityModel(
    userid = userid,// 게시글 id
    title = title, // 제목
    content = content, // 게시글 내용
    profileNickname = profileNickname, // 프로필 닉네임
    profileThumbnail = profileThumbnail, // 프로필 이미지
    views = views, // 조회수
    likes = likes, // 좋아요 수
    key = key, // 게시판 Unique key
    image = image, // 이미지
    likeUsers= likeUsers, // 좋아요 누른 유저
    scrapUsers=scrapUsers // 스크랩한 유저목
)

fun List<CommunityModel?>.toEntity(): List<CommunityEntity?> {
    val list = ArrayList<CommunityEntity>()
    for (i in this.indices) {
        this[i]?.let { list.add(it.toEntity()) }
    }
    return list.toList()
}