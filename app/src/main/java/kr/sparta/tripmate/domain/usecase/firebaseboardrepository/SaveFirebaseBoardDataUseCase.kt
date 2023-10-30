package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

class SaveFirebaseBoardDataUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(
        model: CommunityModelEntity,
        boardLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        repository.saveFirebaseBoardData(model, boardLiveData)
    }
}