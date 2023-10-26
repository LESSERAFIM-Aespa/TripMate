package kr.sparta.tripmate.data.model.community

data class KeyModel(
    val uid: String?,
    val key: String?,
    var myCommuIsLike: Boolean
) {
    constructor() : this(null, null, false)
}
