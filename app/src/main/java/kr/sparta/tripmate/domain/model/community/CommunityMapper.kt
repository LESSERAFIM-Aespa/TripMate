package kr.sparta.tripmate.domain.model.community

import kr.sparta.tripmate.data.model.community.CommunityModel

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
    isLike = isLike,
)
fun List<CommunityModel>.toEntity():List<CommunityEntity> {
    val list = ArrayList<CommunityEntity>()
    for (i in this.indices) {
        list.add(this[i].toEntity())
    }
    return list.toList()
}

fun CommunityEntity.toModel() = CommunityModel(
    key = key,
    title = title,
    description = description,
    userid = userid,
    userNickname = userNickname,
    userThumbnail = userThumbnail,
    views = views,
    likes = likes,
    image = image,
    isLike = isLike,
)
