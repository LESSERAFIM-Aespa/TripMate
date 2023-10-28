package kr.sparta.tripmate.domain.model.firebase

import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.community.CommunityEntity

fun CommunityModel.toEntity() = CommunityEntity(
    key = key,
    title = title,
    description = description,
    userid = userid,
    userNickname = userNickname,
    userThumbnail = userThumbnail,
    views = views,
    likes = likes,
    image = image,
    isLike = isLike
)

fun List<CommunityModel?>.toEntity(): List<CommunityEntity> {
    val list = ArrayList<CommunityEntity>()
    for (i in this.indices) {
        this[i]?.let { list.add(it.toEntity()) }
    }
    return list.toList()
}

fun CommunityEntity.toCommunity() = CommunityModel(
    key = key,
    title = title,
    description = description,
    userid = userid,
    userNickname = userNickname,
    userThumbnail = userThumbnail,
    views = views,
    likes = likes,
    image = image,
    isLike = isLike
)