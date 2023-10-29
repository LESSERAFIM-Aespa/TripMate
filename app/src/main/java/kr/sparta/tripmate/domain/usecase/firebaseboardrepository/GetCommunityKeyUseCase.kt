package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

/**
 * 작성자: 서정한
 * 내용: RDB에서 key값 가져오기
 * */
class GetCommunityKeyUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(): String = repository.getCommunityKey()
}