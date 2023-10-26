package kr.sparta.tripmate.domain.usecase.firebasescraprepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

class GetFirebaseBoardDataFromScrapRepo(private val repository: FirebaseScrapRepository) {
    operator fun invoke(
        uid: String, boardLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) = repository.getFirebaseBoardData(boardLiveData)
}