package kr.sparta.tripmate.ui.userprofile.main

import androidx.fragment.app.Fragment

/**
 * 작성자: 서정한
 * 내용: 사용자프로필에서 사용하는 탭관리용 data class
 * */
data class UserProfileTab(
    val fragment: Fragment,
    val title: Int,
)
