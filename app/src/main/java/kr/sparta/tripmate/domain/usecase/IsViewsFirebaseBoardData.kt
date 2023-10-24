package kr.sparta.tripmate.domain.usecase

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

class IsViewsFirebaseBoardData(private val repository:FirebaseBoardRepository) {
    operator fun invoke(model: CommunityModel, position: Int, commuLiveData:
    MutableLiveData<List<CommunityModelEntity?>>
    ) = repository.isCommuView(model, position, commuLiveData)
}