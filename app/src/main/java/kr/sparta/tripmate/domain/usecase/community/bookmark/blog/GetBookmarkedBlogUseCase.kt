package kr.sparta.tripmate.domain.usecase.community.bookmark.blog

import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository

class GetBookmarkedBlogUseCase(private val repository: FirebaseBookmarkRepository) {
    suspend operator fun invoke(uid: String, url: String) = repository.getBookmarkedBlogByKey(uid, url)
}