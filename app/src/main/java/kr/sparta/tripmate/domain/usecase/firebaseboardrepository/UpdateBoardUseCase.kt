package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository

class UpdateBoardUseCase(private val repository : FirebaseBoardRepository) {
    operator fun invoke(item:CommunityEntity) = repository.updateBoard(item)
}