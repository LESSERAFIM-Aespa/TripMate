package kr.sparta.tripmate.domain.usecase.community.board

import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository

class GetMyBoardsUseCase(private val repository: FirebaseCommunityBoardRepository) {
    suspend operator fun invoke(uid: String) = repository.getMyBoards(uid)
}