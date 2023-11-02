package kr.sparta.tripmate.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDataEntity(
    val login_type : String? = null,
    val login_Id: String? = null,       //로그인 id
    val login_NickName: String? = null, //닉네임
    val login_profile: String? = null,  //프로필사진
    val login_Uid: String? = null,      //고유키값
    val login_coment: String? = null    //자기소개
) : Parcelable
