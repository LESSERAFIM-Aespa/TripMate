package kr.sparta.tripmate.domain.model.scrap

data class ImageServerDataEntity(
    val title: String?,
    val link: String?,
    val description: String?,
    val lastBuildDate: String?,
    val total: Int?,
    val start: Int?,
    val display: Int?,
    val items: ArrayList<ImageItemsEntity>?
)
data class ImageItemsEntity(
    val title: String?,
    val link: String?,
    val thumbnail: String?,
    val sizeHeight: Int?,
    val sizeWidth: Int?
)