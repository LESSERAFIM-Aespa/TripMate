package kr.sparta.tripmate.domain.model.search

import kr.sparta.tripmate.data.model.search.SearchItemModel
import kr.sparta.tripmate.data.model.search.SearchBlogModel
import kr.sparta.tripmate.data.model.search.SearchBlogServerData


fun SearchBlogServerData.toEntity(): ScrapServerDataEntity = ScrapServerDataEntity(
    lastBuildDate = lastBuildDate,
    total = total,
    start = start,
    display = display,
    items = items?.toEntity()
)

fun SearchItemModel.toEntity(): SearchItemsEntity = SearchItemsEntity(
    title = title,
    link = link,
    description = description,
    bloggername = bloggername,
    bloggerlink = bloggerlink,
    postdate = postdate
)

fun ArrayList<SearchItemModel>.toEntity(): ArrayList<SearchItemsEntity> {
    val list = ArrayList<SearchItemsEntity>()
    for (i in this.indices) {
        list.add(this[i].toEntity())
    }
    return list
}

fun SearchItemsEntity.toSearchBlog(): SearchBlogEntity = SearchBlogEntity(
    title = title.orEmpty(),
    url = link.orEmpty(),
    description = description.orEmpty(),
    bloggername = bloggername.orEmpty(),
    bloggerlink = bloggerlink.orEmpty(),
    postdate = postdate.orEmpty(),
)

fun List<SearchItemsEntity>.toSearchBlog() : List<SearchBlogEntity> {
    val list = ArrayList<SearchBlogEntity>()
    for(i in indices) {
        list.add(this[i].toSearchBlog())
    }
    return list.toList()
}

fun SearchItemsEntity.toModel(): SearchBlogModel = SearchBlogModel(
    title = title.orEmpty(),
    url = link.orEmpty(),
    description = description.orEmpty(),
    bloggername = bloggername.orEmpty(),
    bloggerlink = bloggerlink.orEmpty(),
    postdate = postdate.orEmpty(),
)

fun SearchItemsEntity.toEntity(): SearchBlogEntity = SearchBlogEntity(
    title = title.orEmpty(),
    url = link.orEmpty(),
    description = description.orEmpty(),
    bloggername = bloggername.orEmpty(),
    bloggerlink = bloggerlink.orEmpty(),
    postdate = postdate.orEmpty(),
)

fun SearchBlogEntity.toModel() : SearchBlogModel = SearchBlogModel(
    title = title.orEmpty(),
    url = url.orEmpty(),
    description = description.orEmpty(),
    bloggername = bloggername.orEmpty(),
    bloggerlink = bloggerlink.orEmpty(),
    postdate = postdate.orEmpty(),
    isLike = isLike,
)

fun SearchBlogModel.toEntity() : SearchBlogEntity = SearchBlogEntity(
    title = title.orEmpty(),
    url = url.orEmpty(),
    description = description.orEmpty(),
    bloggername = bloggername.orEmpty(),
    bloggerlink = bloggerlink.orEmpty(),
    postdate = postdate.orEmpty(),
    isLike = isLike,
)

fun List<SearchBlogModel>.toEntity() : List<SearchBlogEntity> {
    val list = ArrayList<SearchBlogEntity>()
    for (i in this.indices) {
        list.add(this[i].toEntity())
    }
    return list.toList()
}