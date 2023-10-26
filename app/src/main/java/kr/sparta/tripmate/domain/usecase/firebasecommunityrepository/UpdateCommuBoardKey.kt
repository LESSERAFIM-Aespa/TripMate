package kr.sparta.tripmate.domain.usecase.firebasecommunityrepository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

class UpdateCommuBoardKey(private val repository: FirebaseCommunityRepository) {
    operator fun invoke(
        model: CommunityModel, position: Int, communityLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>, uid: String, context: Context
    ) = repository.updateCommuBoardKey(
        model, position, communityLiveData, boardKeyLiveData, uid,
        context
    )
}