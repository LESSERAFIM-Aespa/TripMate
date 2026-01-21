package kr.sparta.tripmate.domain.model.search

import kr.sparta.tripmate.remote.model.search.SearchBlogModel
import kr.sparta.tripmate.remote.model.search.SearchBlogServerData


fun SearchBlogServerData.toEntity(): SearchBlogServerDataEntity = SearchBlogServerDataEntity(
    lastBuildDate = lastBuildDate,
    total = total,
    start = start,
    display = display,
    items = items?.toEntity()
)

fun SearchBlogModel.toEntity(): SearchBlogEntity = SearchBlogEntity(
    title = title,
    link = link,
    description = description,
    bloggername = bloggername,
    bloggerlink = bloggerlink,
    postdate = postdate,
    isLike = false,
)

fun SearchBlogEntity.toModel(): SearchBlogModel = SearchBlogModel(
    title = title,
    link = link,
    description = description,
    bloggername = bloggername,
    bloggerlink = bloggerlink,
    postdate = postdate,
)

//fun List<SearchBlogModel>.toEntity(): ArrayList<SearchBlogEntity> {
//    val list = ArrayList<SearchBlogEntity>()
//    for (i in this.indices) {
//        list.add(this[i].toEntity())
//    }
//    return list
//}

fun List<SearchBlogModel?>.toEntity(): List<SearchBlogEntity> {
    val list = ArrayList<SearchBlogEntity>()
    for (i in this.indices) {
        this[i]?.let { list.add(it.toEntity()) }
    }
    return list
}
