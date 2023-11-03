package kr.sparta.tripmate.data.repository.community

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toModel
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository

/**
 * 작성자: 서정한
 * 내용: 커뮤니티 게시판 Repository
 * */
class FirebaseBoardRepositoryImpl(
    private val remoteSource: FirebaseBoardRemoteDataSource
) : FirebaseBoardRepository {

    /**
     * 작성자: 서정한
     * 내용: 모든 게시글목록 가져오기
     * */
    override suspend fun getAllBoards(): Flow<List<CommunityModel?>> = remoteSource.getAllBoards()

    /**
     * 작성자: 서정한
     * 내용: 게시글 추가하기(글쓰기)
     * */
    override fun addBoard(item: CommunityEntity) = remoteSource.addBoard(item.toModel())

    /**
     * 작성자: 서정한
     * 내용: 좋아요 버튼클리시 좋아요수 && 좋아요 버튼 UI상태 업데이트
     * */
    override fun updateBoardLike(
        uid: String,
        key: String
    ) {
        remoteSource.updateBoardLike(uid, key)
    }

    /**
     * 작성자: 서정한
     * 내용: 조회수 업데이트
     * */
    override fun updateBoardViews(item: CommunityEntity) {
        val updatedItem = item.copy(views = (item.views ?: 0) + 1)
        remoteSource.updateBoard(
            updatedItem.toModel()
        )
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 게시글 지우기
     * */
    override fun removeBoard(key: String) = remoteSource.removeBoard(key)

    /**
     * 작성자: 서정한
     * 내용: Unique Key생성
     * */
    override fun getKey(): String = remoteSource.getKey()
}