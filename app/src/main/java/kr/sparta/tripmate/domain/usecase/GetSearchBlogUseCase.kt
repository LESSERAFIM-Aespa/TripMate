package kr.sparta.tripmate.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.domain.model.search.SearchBlogServerDataEntity
import kr.sparta.tripmate.domain.repository.BlogRepository
import kr.sparta.tripmate.domain.repository.search.SearchRepository
import javax.inject.Inject

class GetSearchBlogUseCase @Inject constructor(
    private val repository: BlogRepository,
) {
    suspend operator fun invoke(
        query: String,
        display: Int,
        sort: String = "sim",
    ): Flow<SearchBlogServerDataEntity> = repository.getSearchBlog(
        query,
        sort,
        display
    )
}