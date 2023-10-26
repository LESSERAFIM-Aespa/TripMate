package kr.sparta.tripmate.domain.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity

/**
 * 작성자 : 박성수
 * 목적 : 커뮤니티 게시판관련 모든 데이터를 관리합니다.
 * updateCommunityBaseData : 모든 커뮤니티관련 베이스가 됩니다.
 * updateCommuIsLike : 커뮤니티 좋아요를 관리합니다.
 * updateCommuIsView : 커뮤니티 조회수를 관리합니다.
 * updateCommuBoardKey : 커뮤니티 북마크 키를 관리합니다.
 */
interface FirebaseCommunityRepository {
    fun updateCommunityBaseData(
        uid: String, commuLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        keyLiveData: MutableLiveData<List<KeyModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>
    )

    fun updateCommuIsLike(
        model: CommunityModel, position: Int, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, uid: String
    )

    fun updateCommuIsView(
        model: CommunityModel, position: Int, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>
    )

    fun updateCommuBoardKey(
        model: CommunityModel, position: Int, communityLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>, uid: String, context: Context
    )

    fun updateCommunityWrite(item: CommunityModelEntity)

    fun getCommunityKey(): String
}