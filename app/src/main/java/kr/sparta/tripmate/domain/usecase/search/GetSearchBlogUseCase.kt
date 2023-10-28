package kr.sparta.tripmate.domain.usecase.search

import kr.sparta.tripmate.domain.model.search.ScrapServerDataEntity
import kr.sparta.tripmate.domain.repository.search.SearchBlogRepository

class GetSearchBlogUseCase(
    private val repository: SearchBlogRepository,
) {
    suspend operator fun invoke(
        query: String,
        display: Int,
        sort: String = "sim",
    ): ScrapServerDataEntity = repository.getSearchBlog(
        query,
        sort,
        display
    )
}