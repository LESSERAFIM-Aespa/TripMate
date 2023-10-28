package kr.sparta.tripmate.data.repository.community

import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBookmarkRemoteDataSource
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.model.community.toModel
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.model.search.toEntity
import kr.sparta.tripmate.domain.model.search.toModel
import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository

class FirebaseBookmarkRepositoryImpl(private val remoteSource: FirebaseBookmarkRemoteDataSource) :
    FirebaseBookmarkRepository {
    override suspend fun getAllBookmarkedBlogById(uid: String): List<SearchBlogEntity> =
        remoteSource.getAllBookmarkedBlogsById(uid).toEntity()

    override suspend fun getBookmarkedBlogByKey(uid: String, url: String): SearchBlogEntity? =
        remoteSource.getBookmarkedBlogByKey(uid, url)?.toEntity()

    override suspend fun addBookmarkedBlog(uid: String, item: SearchBlogEntity) =
        remoteSource.addBookmarkedBlog(uid, item.toModel())

    override suspend fun updateBookmarkedBlog(uid: String, item: SearchBlogEntity) =
        remoteSource.updateBookmarkedBlog(uid, item.toModel())

    override suspend fun removeBookmarkedBlog(uid: String, url: String) =
        remoteSource.removeBookmarkedBlog(uid, url)

    override suspend fun getAllBookmarkedBoardsById(uid: String): List<CommunityEntity> =
        remoteSource.getAllBookmarkedBoardsById(uid).toEntity()

    override suspend fun getBookmarkedBoardByKey(uid: String, key: String): CommunityEntity? =
        remoteSource.getBookmarkedBoardByKey(uid, key)?.toEntity()

    override suspend fun addBookmarkedBoard(uid: String, item: CommunityEntity) =
        remoteSource.addBookmarkedBoard(uid, item.toModel())

    override suspend fun updateBookmarkedBoard(uid: String, item: CommunityEntity): Boolean =
        remoteSource.updateBookmarkedBoard(uid, item.toModel())

    override suspend fun removeBookmarkedBoard(uid: String, key: String) =
        remoteSource.removeBookmarkedBoard(uid, key)
}