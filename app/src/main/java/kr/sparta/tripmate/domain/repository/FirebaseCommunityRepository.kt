package kr.sparta.tripmate.domain.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity

interface FirebaseCommunityRepository {
    fun getCommunityData(
        uid: String, commuLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        keyLiveData: MutableLiveData<List<KeyModelEntity?>>,  boardKeyLiveData :
        MutableLiveData<List<BoardKeyModelEntity?>>
    )
    fun updateCommuIsLike(
        model: CommunityModel, position: Int, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, uid: String
    )
    fun updateCommuView(model: CommunityModel, position: Int,commuLiveData:
    MutableLiveData<List<CommunityModelEntity?>>)
}