package kr.sparta.tripmate.domain.usecase

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

class GetFirebaseCommunityData(private val repository: FirebaseCommunityRepository) {
    operator fun invoke(
        uid: String, commuLiveData: MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, boardKeyLiveData :
        MutableLiveData<List<BoardKeyModelEntity?>>
    ) = repository.getCommunityData(uid, commuLiveData, keyLiveData)
}