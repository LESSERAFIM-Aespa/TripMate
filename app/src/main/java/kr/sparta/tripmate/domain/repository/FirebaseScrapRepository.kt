package kr.sparta.tripmate.domain.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
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

    /**
     * 작성자 : 박성수
     * 목적 : MyPage의 Scrap 탭 데이터 업데이트를 위해 추가 되었습니다.
     * getFirebaseFromBoardData : 커뮤니티 게시판 데이터 입니다.
     * updateCommuIsView : 커뮤니티 게시판 조회수가 업데이트 됩니다.
     */
    fun getFirebaseBoardData(
        boardLiveData : MutableLiveData<List<CommunityModelEntity?>>
    )
    fun updateCommuIsView(
        model: CommunityModel, position: Int, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>
    )
}