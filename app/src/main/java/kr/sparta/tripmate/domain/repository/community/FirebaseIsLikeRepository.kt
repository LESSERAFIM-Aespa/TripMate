package kr.sparta.tripmate.domain.repository.community

import kr.sparta.tripmate.domain.model.community.CommunityEntity

interface FirebaseIsLikeRepository {
    suspend fun getAllIsLikesById(uid: String): List<CommunityEntity>
    suspend fun getIsLikeByKey(uid: String, key: String): CommunityEntity?
    suspend fun addIsLikeByKey(uid: String, item: CommunityEntity)
    suspend fun updateIsLikeByKey(uid: String, item: CommunityEntity): Boolean
    suspend fun removeIsLikeByKey(uid: String, key: String)
}