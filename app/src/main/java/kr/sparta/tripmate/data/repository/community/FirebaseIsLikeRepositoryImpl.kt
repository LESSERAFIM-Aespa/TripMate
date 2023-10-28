package kr.sparta.tripmate.data.repository.community

import kr.sparta.tripmate.data.datasource.remote.community.FirebaseIsLikeRemoteDataSource
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.model.community.toModel
import kr.sparta.tripmate.domain.repository.community.FirebaseIsLikeRepository

class FirebaseIsLikeRepositoryImpl(private val remoteSource: FirebaseIsLikeRemoteDataSource) :
    FirebaseIsLikeRepository {
    override suspend fun getAllIsLikesById(uid: String): List<CommunityEntity> =
        remoteSource.getAllIsLikesById(uid).toEntity()

    override suspend fun getIsLikeByKey(uid: String, key: String): CommunityEntity? =
        remoteSource.getIsLikeByKey(uid, key)?.toEntity()

    override suspend fun addIsLikeByKey(uid: String, item: CommunityEntity) =
        remoteSource.addIsLikeByKey(uid, item.toModel())

    override suspend fun updateIsLikeByKey(uid: String, item: CommunityEntity):Boolean =
        remoteSource.updateIsLikeByKey(uid, item.toModel())

    override suspend fun removeIsLikeByKey(uid: String, key: String) =
        remoteSource.removedIsLikeByKey(uid, key)
}