package kr.sparta.tripmate.domain.usecase.community.board

import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository

class GetBoardItemUseCase(private val repository: FirebaseCommunityBoardRepository) {
    suspend operator fun invoke(key: String) = repository.getBoardItem(key)
}