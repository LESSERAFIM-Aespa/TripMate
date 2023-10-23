package kr.sparta.tripmate.domain.usecase

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

class IsLikeFirebaseCommunityData(private val repository: FirebaseCommunityRepository) {
    operator fun invoke(
        model: CommunityModel, position: Int, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, uid: String
    ) = repository
        .updateCommuIsLike(model, position, commuLiveData, keyLiveData, uid)
}