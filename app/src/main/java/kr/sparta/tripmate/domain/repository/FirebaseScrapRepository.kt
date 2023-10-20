package kr.sparta.tripmate.domain.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity

/**
 * 작성자: 서정한
 * 내용: Firebase RDB의 Scrap Data Repository
 * */
interface FirebaseScrapRepository {
    // 스크렙데이터 가져오기
    fun getScrapdData(uid: String, liveData: MutableLiveData<List<ScrapEntity?>>)
    fun saveScrapData(uid: String, model: ScrapEntity)
    fun removeScrapData(uid: String, model: ScrapEntity)
}