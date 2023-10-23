import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

class FirebaseCommunityRepositoryImpl(
    private val
    remoteSource: FirebaseDBRemoteDataSource
) : FirebaseCommunityRepository {
    override fun getCommunityData(
        uid: String,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        keyLiveData: MutableLiveData<List<KeyModelEntity?>>,
        boardKeyLiveData: MutableLiveData<List<BoardKeyModelEntity?>>
    ) {
        remoteSource.getCommunityData(uid, commuLiveData, keyLiveData, boardKeyLiveData)
    }

    override fun updateCommuIsLike(
        model: CommunityModel,
        position: Int,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        keyLiveData: MutableLiveData<List<KeyModelEntity?>>,
        uid: String
    ) {
        remoteSource.updateCommuIsLike(model, position, commuLiveData, keyLiveData, uid)
    }

    override fun updateCommuView(
        model: CommunityModel,
        position: Int,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        remoteSource.updateCommuView(model, position, commuLiveData)
    }

    override fun updateCommuBoard(
        model: CommunityModel,
        position: Int,
        communityLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        boardKeyLiveData: MutableLiveData<List<BoardKeyModelEntity?>>,
        uid: String
    ) {
        remoteSource.updateCommuBoard(model, position, communityLiveData, boardKeyLiveData, uid)
    }
}