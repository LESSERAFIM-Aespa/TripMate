package kr.sparta.tripmate.domain.model.firebase

fun BoardKeyModel.toEntity() = BoardKeyModelEntity(
    uid = uid,
    key = key,
    myBoardIsLike = myBoardIsLike
)

fun List<BoardKeyModel>.toEntity() : List<BoardKeyModelEntity>{
    val list = ArrayList<BoardKeyModelEntity>()
    for(i in this.indices){
        this[i]?.let{list.add(it.toEntity())}
    }
    return list.toList()
}