package kr.sparta.tripmate.domain.usecase

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

class UpdateCommuBoard(private val repository: FirebaseCommunityRepository) {
    operator fun invoke(
        model: CommunityModel, position: Int, communityLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>, uid: String, context: Context
    ) = repository.updateCommuBoard(
        model, position, communityLiveData, boardKeyLiveData, uid,
        context
    )
}