package kr.sparta.tripmate.domain.repository.community

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.community.CommunityEntity

/**
 * 작성자: 서정한
 * 내용: 커뮤니티 게시판 Repository
 * */
interface FirebaseBoardRepository {
    /**
     * 작성자: 서정한
     * 내용: 모든 게시글목록 가져오기
     * */
    suspend fun getAllBoards(): Flow<List<CommunityModel?>>

    /**
     * 작성자: 서정한
     * 내용: 게시글 추가하기(글쓰기)
     * */
    fun addBoard(item: CommunityEntity)

    /**
     * 작성자: 서정한
     * 내용: 좋아요 버튼클리시 좋아요수 && 좋아요 버튼 UI상태 업데이트
     * */
    fun updateBoardLike(uid: String, key: String)

    /**
     * 작성자: 서정한
     * 내용: 조회수 업데이트
     * */
    fun updateBoardViews(item: CommunityEntity)

    /**
     * 작성자: 서정한
     * 내용: 선택한 게시글 지우기
     * */
    fun removeBoard(key: String)

    /**
     * 작성자: 서정한
     * 내용: Unique Key생성
     * */
    fun getKey(): String

    fun updateBoard(item: CommunityEntity)
}