package kr.sparta.tripmate.data.repository.community

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.datasource.remote.community.scrap.FirebaseBlogScrapRemoteDataSource
import kr.sparta.tripmate.data.model.search.SearchBlogModel
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.model.search.toModel
import kr.sparta.tripmate.domain.repository.community.FirebaseBlogScrapRepository

/**
 * 작성자: 서정한
 * 내용: 유저가 스크랩한 블로그 Repository
 * */
class FirebaseBlogScrapRepositoryImpl(
    private val remoteSource: FirebaseBlogScrapRemoteDataSource,
) :
    FirebaseBlogScrapRepository {
    /**
     * 작성자 서정한
     * 내용: RDB에서 내가 스크랩한 목록 가져오기
     * */
    override suspend fun getAllBlogScraps(uid: String): Flow<List<SearchBlogModel?>> =
        remoteSource.getAllBlogScrapsFlow(uid)

    /**
     * 작성자 서정한
     * 내용: RDB에 내가 선택한 스크랩 저장
     * */
    override fun updateBlogScrap(uid: String, model: SearchBlogEntity) =
        remoteSource.updateBlogScrap(uid, model.toModel())
}
