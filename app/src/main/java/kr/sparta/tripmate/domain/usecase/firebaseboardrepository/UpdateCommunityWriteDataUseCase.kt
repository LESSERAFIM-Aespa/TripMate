package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

/**
 * 작성자: 서정한
 * 내용: 커뮤니티의 기존 선택 글 업데이트
 * */
class UpdateCommunityWriteDataUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(item: CommunityModelEntity) = repository.updateCommunityWrite(item)
}