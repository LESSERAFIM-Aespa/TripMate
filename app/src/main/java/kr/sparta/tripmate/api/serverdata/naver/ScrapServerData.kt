package kr.sparta.tripmate.api.serverdata.naver

data class ScrapServerData(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: ArrayList<ScrapItems>
)
data class ScrapItems(
    val title: String?,
    val link: String?,
    val description: String?,
    val bloggername: String?,
    val bloggerlink: String?,
    val postdate: String?
)