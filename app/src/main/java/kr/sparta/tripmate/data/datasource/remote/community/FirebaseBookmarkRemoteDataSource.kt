package kr.sparta.tripmate.data.datasource.remote.community

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.data.model.search.SearchBlogModel
import kotlin.coroutines.resume

/**
 * 작성자: 서정한
 * 내용: 북마크된 목록 가져오기
 * */
class FirebaseBookmarkRemoteDataSource {
    private val fireDatabase = Firebase.database

    /**
     * 작성자: 서정한
     * 내용: id가 가진 모든 북마크된 블로그 목록 가져오기
     * */
    suspend fun getAllBookmarkedBlogsById(uid: String): List<SearchBlogModel> {
        val result = suspendCancellableCoroutine<List<SearchBlogModel>> { continuation ->
            val ref = fireDatabase.getReference("Bookmark").child("BlogData").child(uid)
            ref.get()
                .addOnSuccessListener {
                    val list = ArrayList<SearchBlogModel>()
                    for (item in it.children) {
                        val model = item.getValue(SearchBlogModel::class.java)
                        model?.let { it1 ->
                            list.add(it1)
                        }
                    }
                    continuation.resume(list.toList().orEmpty())
                }
        }
        return result.orEmpty()
    }

    /**
     * 작성자: 서정한
     * 내용: id가 가진 북마크된 블로그 목록중 선택한 블로그 가져오기
     * */
    suspend fun getBookmarkedBlogByKey(uid: String, url: String): SearchBlogModel? {
        val list = getAllBookmarkedBlogsById(uid)

        return list.find { it.url == url }
    }

    /**
     * 작성자: 서정한
     * 내용: 북마크 목록에 아이템 추가
     * */
    suspend fun addBookmarkedBlog(uid: String, item: SearchBlogModel) {
        val ref = fireDatabase.getReference("Bookmark").child("BlogData").child(uid)
        val list = getAllBookmarkedBlogsById(uid).toMutableList()
        list.add(item)
        ref.setValue(list)
    }

    /**
     * 작성자: 서정한
     * 내용: 북마크 목록 업데이트. 없으면 추가 있으면 삭제
     * */
    suspend fun updateBookmarkedBlog(uid: String, item: SearchBlogModel) {
        val list = getAllBookmarkedBlogsById(uid).toMutableList()
        val isExist = list.firstOrNull { it.url == item.url }

        // 있을경우 북마크에 추가된 블로그 삭제
        if(isExist != null) {
            removeBookmarkedBlog(uid, item.url)
            return
        }
        // 없을경우 추가
        addBookmarkedBlog(uid, item)
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 북마크된 블로그 삭제
     * */
    suspend fun removeBookmarkedBlog(uid: String, url: String) {
        val ref = fireDatabase.getReference("Bookmark").child("BlogData").child(uid)
        val list = getAllBookmarkedBlogsById(uid).toMutableList()
        list.find { it.url == url }?.let {
            list.remove(it)
        }
        ref.setValue(list)
    }

    /**
     * 작성자: 서정한
     * 내용: id가 가진 모든 북마크된 게시글 목록 가져오기
     * */
    suspend fun getAllBookmarkedBoardsById(uid: String):List<CommunityModel> {
        val result = suspendCancellableCoroutine<List<CommunityModel>> { continuation ->
            val ref = fireDatabase.getReference("Bookmark").child("BoardData").child(uid)
            ref.get()
                .addOnSuccessListener {
                    val list = ArrayList<CommunityModel>()
                    for (item in it.children) {
                        val model = item.getValue(CommunityModel::class.java)
                        model?.let { it1 ->
                            list.add(it1)
                        }
                    }
                    continuation.resume(list.toList().orEmpty())
                }
        }
        return result.orEmpty()
    }

    /**
     * 작성자: 서정한
     * 내용: id가 가진 북마크된 게시글 목록중 선택한 게시글 가져오기
     * */
    suspend fun getBookmarkedBoardByKey(uid: String, key: String): CommunityModel? {
        val list = getAllBookmarkedBoardsById(uid).toMutableList()

        return list.find { it.key == key }
    }

    /**
     * 작성자: 서정한
     * 내용: id가 가진 북마크된 게시글 목록중 선택한 게시글 가져오기
     * */
    suspend fun addBookmarkedBoard(uid: String, item: CommunityModel){
        val ref = fireDatabase.getReference("Bookmark").child("BoardData").child(uid)
        val list = getAllBookmarkedBoardsById(uid).toMutableList()
        list.add(item)
        ref.setValue(list)
    }

    /**
     * 작성자: 서정한
     * 내용: 게시판 북마크 업데이트. 없으면 추가 있으면 삭제
     * */
    suspend fun updateBookmarkedBoard(uid: String, item: CommunityModel): Boolean {
        val list = getAllBookmarkedBoardsById(uid).toMutableList()
        val isExist = list.firstOrNull { it.key == item.key }

        // 있을경우 북마크에 추가된 블로그 삭제
        if(isExist != null) {
            removeBookmarkedBoard(uid, item.key.toString())
            return false
        }
        // 없을경우 추가
        addBookmarkedBoard(uid, item)
        return true
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 북마크된 게시글 삭제
     * */
    suspend fun removeBookmarkedBoard(uid: String, key: String) {
        val ref = fireDatabase.getReference("Bookmark").child("BoardData").child(uid)
        val list = getAllBookmarkedBoardsById(uid).toMutableList()
        list.find { it.key == key }?.let {
            list.remove(it)
        }

        ref.setValue(list)
    }
}