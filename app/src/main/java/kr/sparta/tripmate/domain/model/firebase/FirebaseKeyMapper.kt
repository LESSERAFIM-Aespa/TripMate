package kr.sparta.tripmate.domain.model.firebase

import kr.sparta.tripmate.data.model.community.KeyModel

fun KeyModel.toEntity() = KeyModelEntity(
    uid = uid,
    key = key,
    myCommuIsLike = myCommuIsLike
)
fun List<KeyModel>.toEntity() : List<KeyModelEntity>{
    val list = ArrayList<KeyModelEntity>()
    for(i in this.indices){
        this[i]?.let{list.add(it.toEntity())}
    }
    return list.toList()
}