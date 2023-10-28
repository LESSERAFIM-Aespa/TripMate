package kr.sparta.tripmate.domain.model.search

data class ScrapServerDataEntity(
    val lastBuildDate: String?,
    val total: Int?,
    val start: Int?,
    val display: Int?,
    val items: ArrayList<SearchItemsEntity>?
)

data class SearchItemsEntity(
    val title: String?,
    val link: String?,
    val description: String?,
    val bloggername: String?,
    val bloggerlink: String?,
    val postdate: String?
)