package kr.sparta.tripmate.domain.model.user

import kr.sparta.tripmate.data.model.user.UserData

fun UserData.toEntity() = UserDataEntity(
    type = login_type,
    email = login_Id,
    nickname = login_NickName,
    uid = login_Uid,
    profileImg = login_profile,
    comment = login_coment
)

fun UserDataEntity.toModel() = UserData(
    login_type = type,
    login_Id = email,
    login_NickName = nickname,
    login_Uid = uid,
    login_profile = profileImg,
    login_coment = comment
)

fun List<UserData>.toEntity() :List<UserDataEntity>{
    val list = ArrayList<UserDataEntity>()
    for(i in this.indices){
        this[i]?.let { list.add(it.toEntity()) }
    }
    return list.toList()
}