package kr.sparta.tripmate.domain.usecase.community.bookmark.board

import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository

class GetBookmarkedBoardUseCase(private val repository: FirebaseBookmarkRepository) {
    suspend operator fun invoke(uid:String, key:String) = repository.getBookmarkedBoardByKey(uid, key)
}