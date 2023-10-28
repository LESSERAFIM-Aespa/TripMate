package kr.sparta.tripmate.domain.usecase.community.board

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository

class GetAllBoardsUseCase(private val repository: FirebaseCommunityBoardRepository) {
    suspend operator fun invoke(): List<CommunityEntity> = repository.getAllBoards()
}