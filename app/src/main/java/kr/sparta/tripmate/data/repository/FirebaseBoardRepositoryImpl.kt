package kr.sparta.tripmate.data.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

class FirebaseBoardRepositoryImpl(
    private val
    remoteSource: FirebaseDBRemoteDataSource
) : FirebaseBoardRepository {
    override fun getFirebaseBoardData(
        uid: String,
        boardLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        remoteSource.getFirebaseBoardData(uid, boardLiveData)
    }

    override fun isCommuView(
        model: CommunityModel,
        position: Int,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        remoteSource.updateCommuView(model, position, commuLiveData)
    }
}