package kr.sparta.tripmate.domain.usecase.firebasecommunityrepository

import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

/**
 * 작성자: 서정한
 * 내용: 커뮤니티의 기존 선택 글 업데이트
 * */
class UpdateCommunityWriteData(private val repository: FirebaseCommunityRepository) {
    operator fun invoke(item: CommunityModelEntity) = repository.updateCommunityWrite(item)
}