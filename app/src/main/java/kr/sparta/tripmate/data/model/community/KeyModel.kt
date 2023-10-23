package kr.sparta.tripmate.data.model.board

data class KeyModel(
    val uid: String?,
    val key: String?,
    var myCommuIsLike: Boolean
) {
    constructor() : this(null, null, false)
}
