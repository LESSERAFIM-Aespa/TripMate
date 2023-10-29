package kr.sparta.tripmate.data.repository

import androidx.lifecycle.MutableLiveData
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository

/**
 * 작성자 : 박성수
 * getFirebaseBoardData : 내가쓴글 목록을 불러옵니다.
 * updateCommuIsView : 게시판을 클릭했을때 조회수가 업데이트 됩니다.
 */
class FirebaseBoardRepositoryImpl(
    private val
    remoteSource: FirebaseDBRemoteDataSource
) : FirebaseBoardRepository {
    override fun getFirebaseBoardData(
        boardLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        remoteSource.getFirebaseBoardData(boardLiveData)
    }

    override fun updateCommuIsView(
        model: CommunityModel,
        position: Int,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        remoteSource.updateCommuIsView(model, position, commuLiveData)
    }

    override fun saveBoardFirebase(
        model: CommunityModelEntity,
        boardLiveData: MutableLiveData<List<CommunityModelEntity?>>
    ) {
        remoteSource.saveBoardFirebase(model, boardLiveData)
    }
}