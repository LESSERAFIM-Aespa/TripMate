import android.content.Context
import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseCommunityRepository

/**
 * 작성자 : 박성수
 * updateCommunityBaseData : 커뮤니티 게시판 공용 데이터 및 좋아요와 조회수를 식별하는 키를 모두 업데이트합니다.
 * updateCommuIsLike : 커뮤니티 게시판 좋아요 데이터를 업데이트합니다.
 * updateCommuIsView : 커뮤니티 게시판 조회수 데이터를 업데이트합니다.
 * updateCommuBoardKey : 커뮤니티 게시판 북마크 키를 업데이트합니다.
 */
class FirebaseCommunityRepositoryImpl(
    private val
    remoteSource: FirebaseDBRemoteDataSource
) : FirebaseCommunityRepository {
    override fun updateCommunityBaseData(
        uid: String,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        keyLiveData: MutableLiveData<List<KeyModelEntity?>>,
        boardKeyLiveData: MutableLiveData<List<BoardKeyModelEntity?>>
    ) {
        remoteSource.updateCommunityBaseData(uid, commuLiveData, keyLiveData, boardKeyLiveData)
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

    override fun updateCommuIsView(
        model: CommunityModel,
        position: Int,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        remoteSource.updateCommuIsView(model, position, commuLiveData)
    }

    override fun updateCommuBoardKey(
        model: CommunityModel,
        position: Int,
        communityLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        boardKeyLiveData: MutableLiveData<List<BoardKeyModelEntity?>>,
        uid: String, context: Context
    ) {
        remoteSource.updateCommuBoardKey(
            model, position, communityLiveData, boardKeyLiveData, uid,
            context
        )
    }

    override fun updateCommunityWrite(item: CommunityModelEntity) = remoteSource.updateCommunityWrite(item)
    override fun getCommunityKey(): String = remoteSource.getCommunityKey()
}