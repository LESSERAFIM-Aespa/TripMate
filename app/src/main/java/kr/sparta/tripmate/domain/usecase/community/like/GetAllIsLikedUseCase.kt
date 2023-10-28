package kr.sparta.tripmate.domain.usecase.community.like

import kr.sparta.tripmate.domain.repository.community.FirebaseIsLikeRepository

class GetAllIsLikedUseCase(private val repository: FirebaseIsLikeRepository) {
    suspend operator fun invoke(uid: String) = repository.getAllIsLikesById(uid)
}