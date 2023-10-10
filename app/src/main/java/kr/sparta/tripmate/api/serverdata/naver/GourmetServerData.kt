package kr.sparta.tripmate.api.serverdata.naver

data class GourmetServerData(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: ArrayList<GourmetItem>
)
data class GourmetItem(
    val title: String,
    val link: String,
    val category: String,
    val description: String,
    val telephone: String?,
    val address: String,
    val roadAddress: String,
    val mapx: Int,
    val mapy: Int
)
