package kr.sparta.tripmate.domain.usecase

import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

/**
 * 작성자 서정한
 * 내용: rdb에 내가 선택한 스크랩 삭제
 * */
class RemoveFirebaseScrapData(private val repository: FirebaseScrapRepository) {
    operator fun invoke(uid: String, model: ScrapEntity) = repository.removeScrapData(uid, model)
}