package kr.sparta.tripmate.domain.usecase.community.bookmark.board

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository

class UpdateBookmarkedBoardUseCase(private val repository:FirebaseBookmarkRepository) {
    suspend operator fun invoke(uid: String, item: CommunityEntity) = repository.updateBookmarkedBoard(uid, item)
}