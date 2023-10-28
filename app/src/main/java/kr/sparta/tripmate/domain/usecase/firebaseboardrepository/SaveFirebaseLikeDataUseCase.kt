package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

class SaveFirebaseLikeDataUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(
        model: CommunityModelEntity, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, uid: String
    ) = repository
        .saveFirebaseLikeData(model, commuLiveData, keyLiveData, uid)
}