package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import javax.inject.Inject

/**
* 작성자 : 박성수
* 내용 : 선택한 게시글 1개만 얻어옵니다.
*/
class GetBoardUseCase @Inject constructor(private val repository: FirebaseBoardRepository) {
    operator fun invoke(key: String) = repository.getBoard(key)
}