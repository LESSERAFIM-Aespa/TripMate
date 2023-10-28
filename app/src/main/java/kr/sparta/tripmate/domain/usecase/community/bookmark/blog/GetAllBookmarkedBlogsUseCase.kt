package kr.sparta.tripmate.domain.usecase.community.bookmark.blog

import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository

class GetAllBookmarkedBlogsUseCase(private val repository: FirebaseBookmarkRepository) {
    suspend operator fun invoke(uid: String) = repository.getAllBookmarkedBlogById(uid)
}
