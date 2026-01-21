package kr.sparta.tripmate.remote.model.search

data class ImageServerData(
    val title: String?,
    val link: String?,
    val description: String?,
    val lastBuildDate: String?,
    val total: Int?,
    val start: Int?,
    val display: Int?,
    val items: ArrayList<ImageItems>?
)

data class ImageItems(
    val title: String?,
    val link: String?,
    val thumbnail: String?,
    val sizeHeight: Int?,
    val sizeWidth: Int?
)
