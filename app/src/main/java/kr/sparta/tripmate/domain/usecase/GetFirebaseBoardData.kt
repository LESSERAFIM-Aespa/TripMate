package kr.sparta.tripmate.domain.usecase

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

class GetFirebaseBoardData(private val repository: FirebaseBoardRepository) {
    operator fun invoke(
        uid: String, boardLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) = repository.getFirebaseBoardData(uid, boardLiveData)
}