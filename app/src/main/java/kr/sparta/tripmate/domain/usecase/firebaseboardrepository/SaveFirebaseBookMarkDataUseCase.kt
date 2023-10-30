package kr.sparta.tripmate.domain.usecase.firebaseboardrepository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

class SaveFirebaseBookMarkDataUseCase(private val repository: FirebaseBoardRepository) {
    operator fun invoke(
        model: CommunityModelEntity, uid: String, context: Context, communityLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>
    ) = repository.saveFirebaseBookMarkData(
        model, uid, context, communityLiveData, boardKeyLiveData
    )
}