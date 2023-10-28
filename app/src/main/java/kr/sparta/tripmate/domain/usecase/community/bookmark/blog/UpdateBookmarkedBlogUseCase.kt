package kr.sparta.tripmate.domain.usecase.community.bookmark.blog

import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository

class UpdateBookmarkedBlogUseCase(private val repository: FirebaseBookmarkRepository) {
    suspend operator fun invoke(uid: String, item: SearchBlogEntity) = repository.updateBookmarkedBlog(uid, item)
}