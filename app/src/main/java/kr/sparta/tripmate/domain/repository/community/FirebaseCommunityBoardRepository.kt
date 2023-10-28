package kr.sparta.tripmate.domain.repository.community

import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.community.CommunityEntity

interface FirebaseCommunityBoardRepository {

    suspend fun getAllBoards() : List<CommunityEntity>
    suspend fun getMyBoards(uid: String) : List<CommunityEntity>
    suspend fun getBoardItem(key: String) : CommunityEntity?
    suspend fun addBoardItem(item: CommunityEntity)
    suspend fun updateBoardItem(item:CommunityEntity)
    suspend fun removeBoardItem(key: String)
    suspend fun updateBoardViews(item: CommunityEntity)
    suspend fun updateBoardLikes(item: CommunityEntity, isLike: Boolean)
    fun getCommunityKey(): String
}