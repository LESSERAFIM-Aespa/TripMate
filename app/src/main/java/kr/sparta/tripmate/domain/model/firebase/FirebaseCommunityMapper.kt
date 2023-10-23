package kr.sparta.tripmate.domain.model.firebase

import kr.sparta.tripmate.data.model.community.CommunityModel

fun CommunityModel.toEntity() = CommunityModelEntity(
    id = id,
    thumbnail = thumbnail,
    title = title,
    body = body,
    profileNickname = profileNickname,
    profileThumbnail = profileThumbnail,
    views = views,
    likes = likes,
    key = key,
    addedImage = addedImage,
    commuIsLike = commuIsLike,
    boardLike = boardLike
)
fun List<CommunityModel>.toEntity() : List<CommunityModelEntity>{
    val list = ArrayList<CommunityModelEntity>()
    for(i in this.indices){
        this[i]?.let{list.add(it.toEntity())}
    }
    return list.toList()
}

fun CommunityModelEntity.toCommunity() = CommunityModel(
    id = id,
    thumbnail = thumbnail,
    title = title,
    body = body,
    profileNickname = profileNickname,
    profileThumbnail = profileThumbnail,
    views = views,
    likes = likes,
    key = key,
    addedImage = addedImage,
    commuIsLike = commuIsLike,
    boardLike = boardLike
)