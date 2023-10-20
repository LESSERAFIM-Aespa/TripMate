package kr.sparta.tripmate.domain.model.firebase

import kr.sparta.tripmate.data.model.scrap.ScrapModel

fun ScrapModel.toEntity() = ScrapEntity(
    title = title,
    url = url,
    description = description,
    bloggername = bloggername,
    bloggerlink = bloggerlink,
    postdate = postdate,
    isLike = isLike,
)

fun List<ScrapModel?>.toEntity(): List<ScrapEntity> {
    val list = ArrayList<ScrapEntity>()
    for(i in this.indices) {
        this[i]?.let { list.add(it.toEntity()) }
    }
    return list.toList()
}