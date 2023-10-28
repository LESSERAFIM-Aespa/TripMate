package kr.sparta.tripmate.domain.usecase.community.like

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseIsLikeRepository

class AddIsLikedUseCase(private val repository: FirebaseIsLikeRepository) {
    suspend operator fun invoke(uid: String, item: CommunityEntity) = repository.addIsLikeByKey(uid, item)
}