package kr.sparta.tripmate.data.repository.community

import kr.sparta.tripmate.data.datasource.remote.community.FirebaseCommunityBoardsRemoteDataSource
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.model.community.toModel
import kr.sparta.tripmate.domain.repository.community.FirebaseCommunityBoardRepository

class FirebaseCommunityBoardRepositoryImpl(private val remoteSource: FirebaseCommunityBoardsRemoteDataSource) :
    FirebaseCommunityBoardRepository {
    override suspend fun getAllBoards(): List<CommunityEntity> =
        remoteSource.getAllBoards().toEntity()

    override suspend fun getMyBoards(uid: String): List<CommunityEntity> =
        remoteSource.getMyBoards(uid).toEntity()

    override suspend fun getBoardItem(key: String): CommunityEntity? =
        remoteSource.getBoardItem(key)?.toEntity()

    override suspend fun addBoardItem(item: CommunityEntity) =
        remoteSource.addBoardItem(item.toModel())

    override suspend fun updateBoardItem(item: CommunityEntity) =
        remoteSource.updateBoardItem(item.toModel())

    override suspend fun removeBoardItem(key: String) = remoteSource.removeBoardItem(key)

    override suspend fun updateBoardViews(item: CommunityEntity) =
        remoteSource.updateBoardViews(item.toModel())

    override suspend fun updateBoardLikes(item: CommunityEntity, isLike: Boolean) =
        remoteSource.updateBoardLikes(item.toModel(), isLike)

    override fun getCommunityKey(): String = remoteSource.getCommunityKey()
}
