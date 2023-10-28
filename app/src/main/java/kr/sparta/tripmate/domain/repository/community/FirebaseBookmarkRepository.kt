package kr.sparta.tripmate.domain.repository.community

import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity

interface FirebaseBookmarkRepository {
    suspend fun getAllBookmarkedBlogById(uid: String): List<SearchBlogEntity>
    suspend fun getBookmarkedBlogByKey(uid: String, url: String): SearchBlogEntity?
    suspend fun addBookmarkedBlog(uid: String, item: SearchBlogEntity)
    suspend fun updateBookmarkedBlog(uid: String, item: SearchBlogEntity)
    suspend fun removeBookmarkedBlog(uid: String, url: String)

    suspend fun getAllBookmarkedBoardsById(uid: String): List<CommunityEntity>
    suspend fun getBookmarkedBoardByKey(uid: String, key: String): CommunityEntity?
    suspend fun addBookmarkedBoard(uid: String, item: CommunityEntity)
    suspend fun updateBookmarkedBoard(uid: String, item: CommunityEntity): Boolean
    suspend fun removeBookmarkedBoard(uid: String, key: String)

}