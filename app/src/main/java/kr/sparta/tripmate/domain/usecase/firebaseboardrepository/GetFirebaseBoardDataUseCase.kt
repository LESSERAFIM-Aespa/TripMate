package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

class GetFirebaseBoardDataUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(
        uid: String,
        boardLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        likeKeyLiveData: MutableLiveData<List<KeyModelEntity?>>
    ) = repository.getFirebaseBoardData(uid, boardLiveData, likeKeyLiveData)
}