package kr.sparta.tripmate.data.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseBlogScrapRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.scrap.toScrapModel
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

/**
 * 작성자: 서정한
 * 내용: 유저가 스크랩한 블로그, 게시글 Repository
 * */
class FirebaseScrapRepositoryImpl(
    private val boardRemoteSource: FirebaseBoardRemoteDataSource,
    private val blogRemoteSourece: FirebaseBlogScrapRemoteDataSource,
    ) :
    FirebaseScrapRepository {
    /**
     * 작성자 서정한
     * 내용: RDB에서 내가 스크랩한 목록 가져오기
     * */
    override fun getScrapdData(uid: String, liveData: MutableLiveData<List<ScrapEntity?>>) =
        blogRemoteSourece.getScrapedData(uid, liveData)

    /**
     * 작성자 서정한
     * 내용: RDB에 내가 선택한 스크랩 저장
     * */
    override fun saveScrapData(uid: String, model: ScrapEntity) =
        blogRemoteSourece.saveScrap(uid, model.toScrapModel())

    /**
     * 작성자 서정한
     * 내용: rdb에 내가 선택한 스크랩 삭제
     * */
    override fun removeScrapData(uid: String, model: ScrapEntity) =
        blogRemoteSourece.removeScrap(uid, model)
}
