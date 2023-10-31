package kr.sparta.tripmate.domain.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.login.UserDataEntity

/**
 * 작성자: 서정한
 * 내용: 유저가 스크랩한 블로그, 게시글 Repository
 * */
interface FirebaseScrapRepository {
    // 스크렙데이터 가져오기
    fun getScrapdData(uid: String, liveData: MutableLiveData<List<ScrapEntity?>>)
    fun saveScrapData(uid: String, model: ScrapEntity)
    fun removeScrapData(uid: String, model: ScrapEntity)
}