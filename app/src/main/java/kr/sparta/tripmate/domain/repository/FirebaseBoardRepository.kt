package kr.sparta.tripmate.domain.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity

/**
 * 작성자: 서정한
 * 내용: 커뮤니티 게시판 Repository
 * */
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
        model: CommunityModelEntity, uid: String, context: Context, communityLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>
    )
    fun updateCommunityWrite(item: CommunityModelEntity)
    fun getCommunityKey(): String

}