package kr.sparta.tripmate.domain.usecase.community.board

import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository

class GetCommunityKeyUseCase(private val repository: FirebaseCommunityBoardRepository) {
    operator fun invoke() : String = repository.getCommunityKey()
}