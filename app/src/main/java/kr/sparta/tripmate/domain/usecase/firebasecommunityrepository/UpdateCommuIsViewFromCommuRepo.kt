package kr.sparta.tripmate.domain.usecase.firebasecommunityrepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

class UpdateCommuIsViewFromCommuRepo(private val repository : FirebaseCommunityRepository) {
    operator fun invoke(model: CommunityModel, position: Int, commuLiveData:
    MutableLiveData<List<CommunityModelEntity?>>
    ) = repository.updateCommuIsView(model, position, commuLiveData)
}