package kr.sparta.tripmate.domain.usecase.community.board

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository

/**
 * 작성자: 서정한
 * 내용: 게시글 업데이트
 * */
class UpdateBoardItemUseCase(private val repository: FirebaseCommunityBoardRepository) {
    suspend operator fun invoke(item: CommunityEntity) = repository.updateBoardItem(item)
}