package kr.sparta.tripmate.domain.usecase

import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

/**
 * 작성자 서정한
 * 내용: RDB에 내가 선택한 스크랩 저장
 * */

class SaveFirebaseScrapData(private val repository: FirebaseScrapRepository) {
    operator fun invoke(uid: String, model: ScrapEntity) = repository.saveScrapData(uid, model)
}