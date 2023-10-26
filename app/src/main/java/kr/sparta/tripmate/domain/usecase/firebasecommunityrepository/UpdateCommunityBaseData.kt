package kr.sparta.tripmate.domain.usecase.firebasecommunityrepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

class UpdateCommunityBaseData(private val repository: FirebaseCommunityRepository) {
    operator fun invoke(
        uid: String, commuLiveData: MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>
    ) = repository.updateCommunityBaseData(uid, commuLiveData, keyLiveData, boardKeyLiveData)
}