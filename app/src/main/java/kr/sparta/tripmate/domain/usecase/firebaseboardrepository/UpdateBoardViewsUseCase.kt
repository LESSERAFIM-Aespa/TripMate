package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import javax.inject.Inject

/**
 * 작성자: 서정한
 * 내용: 게시글 조회수 업데이트
 * */
class UpdateBoardViewsUseCase @Inject constructor(private val repository: FirebaseBoardRepository) {
    operator fun invoke(item: CommunityEntity) = repository.updateBoardViews(item)
}