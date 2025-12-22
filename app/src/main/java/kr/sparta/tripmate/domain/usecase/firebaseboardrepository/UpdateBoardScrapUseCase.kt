package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.repository.community.FirebaseBoardScrapRepository
import javax.inject.Inject

/**
 * 작성자: 서정한
 * 내용: 해당 게시물의 스크랩목록을 업데이트합니다.
 * */
class UpdateBoardScrapUseCase @Inject constructor(private val repository: FirebaseBoardScrapRepository) {
    operator fun invoke(uid: String, key: String) = repository.updateBoardScrap(uid, key)
}