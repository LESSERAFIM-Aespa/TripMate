package kr.sparta.tripmate.data.model.budget

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kr.sparta.tripmate.ui.budget.budgetdetail.procedure.ProcedureModel

/**
 * 작성자 : 김성환
 * 가계부의 과정 클래스입니다.
 * 가계부작성시 수입이나 지출에 관련한 데이터가 들어갑니다.
 * 카테고리(Category)의 num 과 과정(Procedure)의 categoryNum은 ForeignKey로 연결되있으며
 * 부모의 카테고리가 삭제될경우 자식인 과정또한 삭제됩니다.
 *
 * 주의 : ForeignKey로 지정되있기때문에 db에 없는 부모의 값을 집어넣어 db에 넣을려고하면 오류가 생깁니다.
 * */
@Entity(
    tableName = "PROCEDURE",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["NUM"],
            childColumns = ["CATEGORY_NUM"],
            onDelete = ForeignKey.CASCADE
        )
    ]

)
data class Procedure(
    @ColumnInfo(name = "CATEGORY_NUM")
    val categoryNum: Int,
    @ColumnInfo(name = "NAME")
    val name: String,
    @ColumnInfo(name = "DESCRIPTION")
    val description: String,
    /**
     * 수입과 지출에 대한 내용이 들어가면됩니다.
     * 지출인경우 양수, 수입인경우 음수가 들어갑니다.
     * */
    @ColumnInfo(name = "MONEY")
    val money: Int,
    /**
     * 해당과정의 시간에대한 값이 들어가면 됩니다
     * yyyy.MM.dd  HH:mm의 형식으로 넣어주면 됩니다.
     * */
    @ColumnInfo(name = "TIME")
    val time: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "NUM")
    val num: Int = 0,
)

/**
 * 작성자: 서정한
 * 내용: view에서 사용하기위해 ProcedureModel로 변환
 * */

fun Procedure.toModel() = ProcedureModel(
    num = num,
    title = name,
    price = money,
    beforeMoney = 0,
    totalAmount = 0,
    time = time,
    categoryColor = "",
    categoryName = "",
)