package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository

/**
 * 작성자: 서정한
 * 내용: 새로운글을 작성할경우 게시글 목록에 추가합니다.
 * */
class AddBoardUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(model: CommunityEntity) = repository.addBoard(model)
}