package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository

/**
 * 작성자: 서정한
 * 내용: 내가 누른 좋아요 목록을 업데이트합니다
 * 만약 좋아요 목록에 내가 누른 게시글의 key가 없을경우 추가 후 true를 반환합니다.
 * 게시글의 좋아요 목록이 있을경우 해당 게시글의 boolean값을 반환합니다.
 * */
class UpdateBoardLikeUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(
        uid: String,
        key: String,
    ) {
        return repository.updateBoardLike(
            uid = uid,
            key = key
        )
    }
}