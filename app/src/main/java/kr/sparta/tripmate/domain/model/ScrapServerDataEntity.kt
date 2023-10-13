package kr.sparta.tripmate.domain.model

data class ScrapServerDataEntity(
    val lastBuildDate: String?,
    val total: Int?,
    val start: Int?,
    val display: Int?,
    val items: ArrayList<ScrapItemsEntity>?
)

data class ScrapItemsEntity(
    val title: String?,
    val link: String?,
    val description: String?,
    val bloggername: String?,
    val bloggerlink: String?,
    val postdate: String?
)