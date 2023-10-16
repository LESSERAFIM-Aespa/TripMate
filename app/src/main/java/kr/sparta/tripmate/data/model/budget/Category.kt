package kr.sparta.tripmate.data.model.budget

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * 작성자 : 김성환
 * 가계부 카테고리 클래스입니다.
 * 카테고리의 정보들이 담겨져있습니다.
 * 가계부(Budget)의 num 과 카테고리(Category)의 budgetNum은 ForeignKey로 연결되있으며
 * 부모의 가계부가 삭제될경우 자식인 카테고리또한 삭제됩니다.
 *
 * 주의 : ForeignKey로 지정되있기때문에 db에 없는 부모의 값을 집어넣어 db에 넣을려고하면 오류가 생깁니다.
 * */
@Entity(
    tableName = "CATEGORY",
    foreignKeys = [
        ForeignKey(
            entity = Budget::class,
            parentColumns = ["NUM"],
            childColumns = ["BUDGET_NUM"],
            onDelete =  ForeignKey.CASCADE
        )
    ]
)
data class Category(
    @ColumnInfo(name = "BUDGET_NUM")
    val budgetNum: Int,
    @ColumnInfo(name = "NAME")
    val name: String,
    /**
     * 카테고리의 색깔입니다.
     * ex) #F8BBD0, #EC407A
     * */
    @ColumnInfo(name = "COLOR")
    val color: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "NUM")
    val num: Int = 0,
)
