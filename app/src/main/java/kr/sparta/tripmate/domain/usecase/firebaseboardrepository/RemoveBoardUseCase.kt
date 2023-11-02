package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository

/**
 * 작성자: 서정한
 * 내용: 선택한 게시글을 삭제합니다.
 * */
class RemoveBoardUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(key: String) = repository.removeBoard(key)
}