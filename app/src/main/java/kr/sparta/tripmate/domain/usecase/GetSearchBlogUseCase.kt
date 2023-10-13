package kr.sparta.tripmate.domain.usecase

import kr.sparta.tripmate.domain.model.ScrapServerDataEntity
import kr.sparta.tripmate.domain.repository.ScrapRepository

class GetSearchBlogUseCase(
    private val repository: ScrapRepository
) {
    suspend operator fun invoke(
        query: String,
        sort: String = "sim",
        display: Int = 10,
    ): ScrapServerDataEntity = repository.getSearchBlog(
        query,
        sort,
        display
    )
}