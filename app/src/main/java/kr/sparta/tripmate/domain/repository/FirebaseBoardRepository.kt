package kr.sparta.tripmate.domain.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity

interface FirebaseBoardRepository {
    fun getFirebaseBoardData(
        uid: String, boardLiveData : MutableLiveData<List<CommunityModelEntity?>>
    )
    fun isCommuView(
        model: CommunityModel, position: Int, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>
    )
}