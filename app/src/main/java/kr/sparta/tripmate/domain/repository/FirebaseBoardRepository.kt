package kr.sparta.tripmate.domain.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity

/**
 * 작성자 : 박성수
 * 목적 : 단순히 업데이트된 커뮤니티 데이터와 조회수가 증가됩니다.
 * getFirebaseBoardData : 커뮤니티 게시판관련 데이터를 불러옵니다.
 * updateCommuIsView : 게시판 클릭 시 조회수가 업데이트 됩니다.
 */
interface FirebaseBoardRepository {
    fun getFirebaseBoardData(
        uid: String,
        boardLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        likeKeyLiveData: MutableLiveData<List<KeyModelEntity?>>
    )
    fun saveFirebaseBoardData(
        model: CommunityModelEntity, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>
    )

    fun getFirebaseLikeData(
        uid: String,
        communityData: List<CommunityModelEntity?>,
        communityLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        likeKeyLiveData: MutableLiveData<List<KeyModelEntity?>>
    )
    fun getFirebaseBookMarkData  (
        uid: String,
        boardKeyLiveData: MutableLiveData<List<BoardKeyModelEntity?>>
    )
    fun saveFirebaseLikeData(
        model: CommunityModelEntity, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, uid: String
    )
    fun saveFirebaseBookMarkData(
        model: CommunityModelEntity, communityLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>, uid: String, context: Context
    )
    fun updateCommunityWrite(item: CommunityModelEntity)
    fun getCommunityKey(): String

}