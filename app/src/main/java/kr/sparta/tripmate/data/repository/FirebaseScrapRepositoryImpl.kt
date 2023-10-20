package kr.sparta.tripmate.data.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.scrap.toScrapModel
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

class FirebaseScrapRepositoryImpl(private val remoteSource: FirebaseDBRemoteDataSource) :
    FirebaseScrapRepository {
    /**
     * 작성자 서정한
     * 내용: RDB에서 내가 스크랩한 목록 가져오기
     * */
    override fun getScrapdData(uid: String, liveData: MutableLiveData<List<ScrapEntity?>>) =
        remoteSource.getScrapedData(uid, liveData)

    /**
     * 작성자 서정한
     * 내용: RDB에 내가 선택한 스크랩 저장
     * */
    override fun saveScrapData(uid: String, model: ScrapEntity) = remoteSource.saveScrap(uid, model.toScrapModel())

    /**
     * 작성자 서정한
     * 내용: rdb에 내가 선택한 스크랩 삭제
     * */
    override fun removeScrapData(uid: String, model: ScrapEntity) =
        remoteSource.removeScrap(uid, model)
}