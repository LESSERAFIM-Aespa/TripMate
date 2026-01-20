package kr.sparta.tripmate.api

import kr.sparta.tripmate.BuildConfig

object Constants {
    val naverUrl = "https://openapi.naver.com/v1/"
    val client_id = BuildConfig.NAVER_CLIENT_ID
    val client_secret = BuildConfig.NAVER_CLIENT_SECRET

    var EMPTYTYPE = 0
    var SCRAPTYPE = 1
    var COMMUNITYTYPE = 2
}
