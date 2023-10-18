package kr.sparta.tripmate.domain.model.scrap

import kr.sparta.tripmate.data.model.scrap.ScrapItems
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.data.model.scrap.ScrapServerData

fun ScrapServerData.toEntity(): ScrapServerDataEntity = ScrapServerDataEntity(
    lastBuildDate = lastBuildDate,
    total = total,
    start = start,
    display = display,
    items = items?.toEntity()
)

fun ScrapItems.toEntity(): ScrapItemsEntity = ScrapItemsEntity(
    title = title,
    link = link,
    description = description,
    bloggername = bloggername,
    bloggerlink = bloggerlink,
    postdate = postdate
)

fun ArrayList<ScrapItems>.toEntity(): ArrayList<ScrapItemsEntity> {
    val list = ArrayList<ScrapItemsEntity>()
    for (i in this.indices) {
        list.add(this[i].toEntity())
    }
    return list
}

fun ScrapItemsEntity.toScrapModel(): ScrapModel = ScrapModel(
    title = title.orEmpty(),
    url = link.orEmpty(),
    description = description.orEmpty(),
    bloggername = bloggername.orEmpty(),
    bloggerlink = bloggerlink.orEmpty(),
    postdate = postdate.orEmpty(),
)