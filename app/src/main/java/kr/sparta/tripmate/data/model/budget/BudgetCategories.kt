package kr.sparta.tripmate.data.model.budget

import androidx.room.Embedded
import androidx.room.Relation

/**
 * 작성자 : 김성환
 * 가계부와 가계부와 관계가있는 카테고리의 정보가 같이 들어가는 구조입니다.
 * 해당 클래스의 경우 직접만들지않고, dao에서 사용하기 위해 만들이진 클래스입니다
 * */
data class BudgetCategories(
    @Embedded
    val budget: Budget,
    /**
     * 가계부의 num 과 카테고리의 budgetNum이같은 카테고리만 들어갑니다.
     * */
    @Relation(
        parentColumn = "NUM",
        entityColumn = "BUDGET_NUM"
    )
    val categories: List<Category>?,
)
