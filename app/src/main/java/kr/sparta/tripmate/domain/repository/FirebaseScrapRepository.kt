package kr.sparta.tripmate.domain.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity

/**
 * 작성자: 서정한
 * 내용: Firebase RDB에서 받아온
 * Scrap 데이터 Repository
 * */
interface FirebaseScrapRepository {
    // 스크렙데이터 가져오기
    fun getScrapedData(uid: String, liveData: MutableLiveData<List<ScrapEntity>>)
}