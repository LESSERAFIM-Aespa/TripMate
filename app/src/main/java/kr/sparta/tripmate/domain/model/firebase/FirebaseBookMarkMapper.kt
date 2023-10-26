package kr.sparta.tripmate.domain.model.firebase

fun ScrapEntity.toCommunityEntity() = CommunityModelEntity(
    id = "",
    title = title,
    body = description,
    profileNickname = bloggername,
    profileThumbnail = null,
    views = "",
    likes = "",
    key = url,
    addedImage = "",
    commuIsLike = false,
    boardIsLike = false
)