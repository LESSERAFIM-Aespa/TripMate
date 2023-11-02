package kr.sparta.tripmate.domain.repository.community

/**
 * 작성자: 서정한
 * 내용: 유저가 스크랩한 게시글 Repository
 * */
interface FirebaseBoardScrapRepository {
    /**
     * 작성자: 서정한
     * 내용: 사용자가 스크랩한 게시글 목록을 불러옵니다.
     * */
    fun updateBoardScrap(uid: String, key: String)
}