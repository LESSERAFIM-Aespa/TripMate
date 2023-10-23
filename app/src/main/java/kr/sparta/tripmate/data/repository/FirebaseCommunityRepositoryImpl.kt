import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource

class FirebaseCommunityRepositoryImpl(
    private val
    remoteSource: FirebaseDBRemoteDataSource
) : FirebaseCommunityRepository {
    override fun getCommunityData(
        uid: String,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        keyLiveData: MutableLiveData<List<KeyModelEntity?>>
    ) {
        remoteSource.getCommunityData(uid, commuLiveData, keyLiveData)
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
}