package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

class UpdateCommuIsViewFromBoardRepo(private val repository:FirebaseBoardRepository) {
    operator fun invoke(model: CommunityModel, position: Int, commuLiveData:
    MutableLiveData<List<CommunityModelEntity?>>
    ) = repository.updateCommuIsView(model, position, commuLiveData)
}
