package kr.sparta.tripmate.domain.usecase

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

class GetFirebaseCommunityData(private val repository: FirebaseCommunityRepository) {
    operator fun invoke(
        uid: String, commuLiveData: MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>
    ) = repository.getCommunityData(uid, commuLiveData, keyLiveData)
}