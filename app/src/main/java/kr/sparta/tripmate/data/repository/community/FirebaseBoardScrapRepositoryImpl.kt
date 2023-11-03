package kr.sparta.tripmate.data.repository.community

import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardScrapRepository

/**
 * 작성자: 서정한
 * 내용: 유저가 스크랩한 게시글 Repository
 * */
class FirebaseBoardScrapRepositoryImpl(private val remoteSource: FirebaseBoardRemoteDataSource) :
    FirebaseBoardScrapRepository {
    override fun updateBoardScrap(uid: String, key: String) =
        remoteSource.updateScrapBoards(uid, key)
}