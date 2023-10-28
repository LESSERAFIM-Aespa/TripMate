package kr.sparta.tripmate.domain.usecase.community.board

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository

class AddBoardItemUseCase(private val repository: FirebaseCommunityBoardRepository) {
    suspend operator fun invoke(item: CommunityEntity) = repository.addBoardItem(item)
}