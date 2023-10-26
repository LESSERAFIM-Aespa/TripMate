package kr.sparta.tripmate.ui.userprofile.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 작성자: 서정한
 * 내용: 유저정보창에서 사용할 Data Class
 * */
@Parcelize
data class UserProfileModel(
    val thumbnail: String, // 썸네일 이미지 url
    val nickname: String, // 닉네임
    val selfContent: String, // 한줄자기소개
    val image: String, // 내 게시글 이미지
    val title: String, // 게시글 제목
    val views : String, // 조회수
    val likes : String, // 좋아요수
):Parcelable
