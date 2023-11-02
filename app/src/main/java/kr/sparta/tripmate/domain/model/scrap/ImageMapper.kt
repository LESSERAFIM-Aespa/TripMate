package kr.sparta.tripmate.domain.model.scrap

import kr.sparta.tripmate.data.model.scrap.ImageItems
import kr.sparta.tripmate.data.model.scrap.ImageServerData


fun ImageServerData.toEntity() :ImageServerDataEntity = ImageServerDataEntity(
    title = title,
    link = link,
    description = description,
    lastBuildDate = lastBuildDate,
    total = total,
    start = start,
    display = display,
    items = items?.toEntity()
)

fun ImageItems.toEntity() :ImageItemsEntity = ImageItemsEntity(
    title = title,
    link = link,
    thumbnail = thumbnail,
    sizeHeight = sizeHeight,
    sizeWidth = sizeWidth
)

fun ArrayList<ImageItems>.toEntity(): ArrayList<ImageItemsEntity>{
    val list = ArrayList<ImageItemsEntity>()
    for(i in this.indices){
        list.add(this[i].toEntity())
    }
    return list
}