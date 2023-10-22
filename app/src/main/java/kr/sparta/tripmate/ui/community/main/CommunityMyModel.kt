package kr.sparta.tripmate.ui.community.main

data class CommunityMyModel(
    val uid: String?,
    val key: String?,
    var myCommuIsLike: Boolean
) {
    constructor() : this(null, null, false)
}
