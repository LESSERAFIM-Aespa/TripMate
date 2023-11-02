package kr.sparta.tripmate.domain.model.user

import kr.sparta.tripmate.data.model.user.UserData

fun UserData.toEntity() = UserDataEntity(
    login_type = login_type,
    login_Id = login_Id,
    login_NickName = login_NickName,
    login_Uid = login_Uid,
    login_profile = login_profile,
    login_coment = login_coment
)

fun List<UserData>.toEntity() :List<UserDataEntity>{
    val list = ArrayList<UserDataEntity>()
    for(i in this.indices){
        this[i]?.let { list.add(it.toEntity()) }
    }
    return list.toList()
}