package kr.sparta.tripmate.domain.usecase

import kr.sparta.tripmate.domain.model.scrap.ScrapServerDataEntity
import kr.sparta.tripmate.domain.repository.ScrapRepository

class GetSearchBlogUseCase(
    private val repository: ScrapRepository,
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