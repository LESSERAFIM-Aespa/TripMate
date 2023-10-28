package kr.sparta.tripmate.domain.usecase.community.bookmark.blog

import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository

class AddBookmarkedBlogUseCase(private val repository: FirebaseBookmarkRepository) {
    suspend operator fun invoke(uid: String, item: SearchBlogEntity) = repository.addBookmarkedBlog(uid, item)
}