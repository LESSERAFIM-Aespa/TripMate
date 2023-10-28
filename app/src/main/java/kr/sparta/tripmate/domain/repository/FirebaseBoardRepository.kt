package kr.sparta.tripmate.domain.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity

/**
 * 작성자 : 박성수
 * 목적 : 단순히 업데이트된 커뮤니티 데이터와 조회수가 증가됩니다.
 * getFirebaseBoardData : 커뮤니티 게시판관련 데이터를 불러옵니다.
 * updateCommuIsView : 게시판 클릭 시 조회수가 업데이트 됩니다.
 */
interface FirebaseBoardRepository {
    fun getFirebaseBoardData(boardLiveData: MutableLiveData<List<CommunityModelEntity?>>)

    fun updateCommuIsView(
        model: CommunityModel, position: Int, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>
    )

    fun saveBoardFirebase(
        model: CommunityModelEntity, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>
    )
}