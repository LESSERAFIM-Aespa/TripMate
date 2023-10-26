package kr.sparta.tripmate.data.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.model.scrap.toScrapModel
import kr.sparta.tripmate.domain.repository.FirebaseScrapRepository

class FirebaseScrapRepositoryImpl(private val remoteSource: FirebaseDBRemoteDataSource) :
    FirebaseScrapRepository {
    /**
     * 작성자 서정한
     * 내용: RDB에서 내가 스크랩한 목록 가져오기
     * */
    override fun getScrapdData(uid: String, liveData: MutableLiveData<List<ScrapEntity?>>) =
        remoteSource.getScrapedData(uid, liveData)

    /**
     * 작성자 서정한
     * 내용: RDB에 내가 선택한 스크랩 저장
     * */
    override fun saveScrapData(uid: String, model: ScrapEntity) =
        remoteSource.saveScrap(uid, model.toScrapModel())

    /**
     * 작성자 서정한
     * 내용: rdb에 내가 선택한 스크랩 삭제
     * */
    override fun removeScrapData(uid: String, model: ScrapEntity) =
        remoteSource.removeScrap(uid, model)

    /**
     * 작성자 : 박성수
     * 커뮤니티 게시글을 불러옵니다.
     */
    override fun getFirebaseBoardData(
        boardLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        remoteSource.getFirebaseBoardData(boardLiveData)
    }

    /**
     * 작성자 : 박성수
     * 커뮤니티 게시글 조회수를 증가시킵니다.
     */
    override fun updateCommuIsView(
        model: CommunityModel,
        position: Int,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        remoteSource.updateCommuIsView(model, position, commuLiveData)
    }

    /**
     * 작성자 : 박성수
     * 커뮤니티 게시글 중에 북마크 된 항목을 체크하기 위한 키를 불러옵니다.
     */
    override fun getFirebaseBoardKeyData(
        uid: String, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>
    ) {
        remoteSource.getFirebaseBoardKeyData(uid, boardKeyLiveData)
    }

    override fun updateUserData(
        uid: String, userLiveData:
        MutableLiveData<UserDataEntity?>
    ) {
        remoteSource.updateUserData(uid, userLiveData)
    }

    override fun saveUserData(
        model: UserDataEntity,
        userLiveData: MutableLiveData<UserDataEntity?>
    ) {
        remoteSource.saveUserData(model, userLiveData)
    }

}