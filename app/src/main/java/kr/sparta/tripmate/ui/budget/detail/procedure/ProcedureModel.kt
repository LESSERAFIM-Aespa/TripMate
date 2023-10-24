package kr.sparta.tripmate.ui.budget.detail.procedure

import kr.sparta.tripmate.data.model.budget.Category
import java.util.Date

/**
 * 작성자: 서정한
 * 내용: 가계부 세부 과정에서 사용하는 모델
 * */
data class ProcedureModel(
    val num : Int, // Procedure num(PK)
    val title: String, // 제목
    val price: Int, // 사용금액
    val beforeMoney: Int, // 사용전 남은금액
    val totalAmount: Int, // 현재 총 금액 (사용전 남은금액 - 사용금액)
    val time: String, // 작성 시간
    val categoryName: String, // 카테고리명
    val categoryColor: String, // 카테고리 색상
)
