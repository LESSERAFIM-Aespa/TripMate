package kr.sparta.tripmate.domain.usecase.firebasescraprepository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.search.SearchBlogModel
import kr.sparta.tripmate.domain.repository.community.FirebaseBlogScrapRepository

/**
 * 작성자: 서정한
 * 내용: 내가 스크랩한 블로그목록을 반환합니다.
 * */
class GetAllBlogScrapsUseCase(private val repository: FirebaseBlogScrapRepository) {
    suspend operator fun invoke(uid: String): Flow<List<SearchBlogModel?>> = repository.getAllBlogScraps(uid)
}