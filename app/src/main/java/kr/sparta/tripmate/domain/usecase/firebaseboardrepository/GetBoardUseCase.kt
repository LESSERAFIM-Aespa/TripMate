package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository

class GetBoardUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(key: String) = repository.getBoard(key)
}