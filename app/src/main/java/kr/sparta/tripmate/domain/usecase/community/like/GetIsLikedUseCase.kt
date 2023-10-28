package kr.sparta.tripmate.domain.usecase.community.like

import kr.sparta.tripmate.domain.repository.community.FirebaseIsLikeRepository

class GetIsLikedUseCase(private val repository: FirebaseIsLikeRepository) {
    suspend operator fun invoke(uid: String, key: String) = repository.getIsLikeByKey(uid, key)
}