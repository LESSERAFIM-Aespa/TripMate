package kr.sparta.tripmate.domain.usecase.firebasescraprepository

import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseBlogScrapRepository
import javax.inject.Inject

/**
 * 작성자: 서정한
 * 내용: 스크랩한 블로그 목록을 업데이트합니다.
 * 블로그 목록에 없는 item일 경우 추가하고
 * 있는경우 제거합니다.
 * */
class UpdateBlogScrapUseCase @Inject constructor(private val repository: FirebaseBlogScrapRepository) {
    operator fun invoke(uid: String, model: SearchBlogEntity) =
        repository.updateBlogScrap(uid, model)
}