package kr.sparta.tripmate.domain.usecase.community.board

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository

/**
 * 작성자: 서정한
 * 내용: 게시글 조회수 업데이트
 * */
class UpdateBoardItemViewsUseCase(private val repository: FirebaseCommunityBoardRepository) {
    suspend operator fun invoke(item: CommunityEntity) = repository.updateBoardViews(item)
}