package kr.sparta.tripmate.domain.usecase

import kr.sparta.tripmate.domain.model.search.SearchBlogServerDataEntity
import kr.sparta.tripmate.domain.repository.search.SearchRepository

class GetSearchBlogUseCase(
    private val repository: SearchRepository,
) {
    suspend operator fun invoke(
        query: String,
        display: Int,
        sort: String = "sim",
    ): SearchBlogServerDataEntity = repository.getSearchBlog(
        query,
        sort,
        display
    )
}