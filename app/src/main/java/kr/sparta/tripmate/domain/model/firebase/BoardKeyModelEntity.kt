package kr.sparta.tripmate.domain.model.firebase

data class BoardKeyModelEntity (
    val uid: String? = null,
    val key: String? = null,
    var myBoardIsLike: Boolean = false
)