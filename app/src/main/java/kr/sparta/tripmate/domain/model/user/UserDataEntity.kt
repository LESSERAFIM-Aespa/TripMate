package kr.sparta.tripmate.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDataEntity(
    val type : String? = null,
    val email: String? = null,       //로그인 id
    val nickname: String? = null, //닉네임
    val profileImg: String? = null,  //프로필사진
    val uid: String? = null,      //고유키값
    val comment: String? = null    //자기소개
) : Parcelable
