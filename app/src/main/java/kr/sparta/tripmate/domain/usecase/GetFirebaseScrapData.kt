package kr.sparta.tripmate.domain.usecase

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

/**
 * 작성자: 서정한
 * 내용: Firebase와 통신한 후 받은 SCrapData를
 * 전달해준다.
 * */
class GetFirebaseScrapData(private val repository: FirebaseScrapRepository) {
    operator fun invoke(uid: String, liveData: MutableLiveData<List<ScrapEntity?>>) = repository.getScrapdData(uid, liveData)
}