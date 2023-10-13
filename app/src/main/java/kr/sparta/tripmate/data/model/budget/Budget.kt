package kr.sparta.tripmate.data.model.budget

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * 작성자 : 김성환
 * 가계부 클래스입니다.
 * 가계부의 정보들이 담겨져있습니다.
 * */
@Entity(
    tableName = "BUDGET",
)
data class Budget(
    @ColumnInfo(name = "NAME")
    val name: String,
    /**
     * 가계부의 시작일에대한 정보만 들어가면됩니다.
     * yyyy.MM.dd 의 형식으로 넣어주면 됩니다.
     * */
    @ColumnInfo(name = "START_DATE")
    val startDate: String,
    /**
     * 가계부의 종료일에대한 정보만 들어가면됩니다.
     * yyyy.MM.dd 의 형식으로 넣어주면 됩니다.
     * */
    @ColumnInfo(name = "END_DATE")
    val endDate: String,
    @ColumnInfo(name = "MONEY")
    val money: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "NUM")
    val num: Int = 0,
) {
    /**
     * 가계부의 모든 과정이후 남은 돈을 넣어주면됩니다.
     * 테이블에 등록되지는 않습니다.
     * */
    @Ignore
    var resultMoeny: Int = 0
    override fun toString(): String {
        return "Budget(name=$name, startDate=$startDate, endDate=$endDate, money=$money, num=$num, resultMoney = $resultMoeny)"
    }

}