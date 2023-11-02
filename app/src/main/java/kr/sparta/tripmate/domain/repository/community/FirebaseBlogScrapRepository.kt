package kr.sparta.tripmate.domain.repository.community

import kotlinx.coroutines.flow.Flow
import kr.sparta.tripmate.data.model.search.SearchBlogModel
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity

/**
 * 작성자: 서정한
 * 내용: 유저가 스크랩한 블로그 Repository
 * */
interface FirebaseBlogScrapRepository {
    /**
     * 작성자: 서정한
     * 내용: 스크랩한 블로그 전체목록을 가져옵니다.
     * */
    suspend fun getAllBlogScraps(uid: String): Flow<List<SearchBlogModel?>>

    /**
     * 작성자: 서정한
     * 내용: 블로그 목록을 업데이트 합니다.
     * model이 스크랩 목록에 있을경우 삭제하고 없을경우 추가합니다.
     * */
    fun updateBlogScrap(uid: String, model: SearchBlogEntity)
}