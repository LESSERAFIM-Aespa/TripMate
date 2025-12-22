package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import javax.inject.Inject

/**
 * 작성자: 서정한
 * 내용: 게시글 리스트 가져오기
 * */
class GetAllBoardsUseCase @Inject constructor(private val repository: FirebaseBoardRepository) {
    suspend operator fun invoke(): Flow<List<CommunityModel?>> = repository.getAllBoards()
}