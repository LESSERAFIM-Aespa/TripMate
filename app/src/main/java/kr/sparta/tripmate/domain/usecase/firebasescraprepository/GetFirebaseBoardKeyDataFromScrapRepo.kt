package kr.sparta.tripmate.domain.usecase.firebasescraprepository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

class GetFirebaseBoardKeyDataFromScrapRepo(private val repository: FirebaseScrapRepository) {
    operator fun invoke(
        uid: String,
        boardKeyLiveData: MutableLiveData<List<BoardKeyModelEntity?>>
    ) {
        repository.getFirebaseBoardKeyData(uid, boardKeyLiveData)
    }
}