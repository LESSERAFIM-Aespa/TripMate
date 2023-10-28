package kr.sparta.tripmate.data.datasource.remote.community

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.sparta.tripmate.data.model.community.CommunityModel
import kotlin.coroutines.resume

/**
 * 작성자: 서정한
 * 내용: 게시판 목록 가져오기
 * */
class FirebaseCommunityBoardsRemoteDataSource {
    private val fireDatabase = Firebase.database
    private val PATH_COMMUNITY_BOARD = "CommunityData1"

    /**
     * 작성자: 서정한
     * 내용: 모든 게시판목록을 가져옴
     * */
    suspend fun getAllBoards(): List<CommunityModel> {
        val result = suspendCancellableCoroutine<List<CommunityModel>> { continuation ->
            val ref = fireDatabase.getReference(PATH_COMMUNITY_BOARD)
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
        return result
    }

    /**
     * 작성자: 서정한
     * 내용: 내가쓴글들 가져오기
     * */
    suspend fun getMyBoards(uid: String): List<CommunityModel> {
        val list = getAllBoards()

        return list.filter { it.userid == uid }.toList()
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 게시글 가져오기
     * */
    suspend fun getBoardItem(key: String): CommunityModel? {
        val list = getAllBoards()
        return list.find { it.key == key }
    }

    /**
     * 작성자: 서정한
     * 내용: 게시글 생성
     * */
    suspend fun addBoardItem(item: CommunityModel) {
        val ref = fireDatabase.getReference(PATH_COMMUNITY_BOARD)
        val list = getAllBoards().toMutableList()
        list.add(item)
        ref.setValue(list)
    }


    /**
     * 작성자: 서정한
     * 내용: 선택한 게시글 내용 업데이트
     * */
    suspend fun updateBoardItem(item: CommunityModel) {
        val ref = fireDatabase.getReference(PATH_COMMUNITY_BOARD)
        val list = getAllBoards().toMutableList()

        val getItem = list.find { it.key == item.key }
        val index: Int? = getItem?.let {
            list.indexOf(it)
        }

        index?.let {
            if (index == -1) {
                return@let
            }

            list[index] = item
            ref.setValue(list.toList())
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 게시글 삭제
     * */
    suspend fun removeBoardItem(key: String) {
        val ref = fireDatabase.getReference(PATH_COMMUNITY_BOARD)
        val list = getAllBoards().toMutableList()
        list.find { it.key == key }?.let {
            list.remove(it)
        }
        ref.setValue(list)
    }

    /**
     * 작성자: 서정한
     * 내용: 게시판 글 조회수 증가
     * */
    suspend fun updateBoardViews(item: CommunityModel) {
        val views = item.views?.let { Integer.parseInt(it) + 1 }.toString()
        updateBoardItem(
            item.copy(
                views = views
            )
        )
    }

    /**
     * 작성자: 서정한
     * 내용: 게시판 글 좋아요 수 업데이트
     * */
    suspend fun updateBoardLikes(item: CommunityModel, isLike: Boolean) {
        val getItem = getBoardItem(item.key.toString())
        val count = getItem?.likes?.let { Integer.parseInt(it) } ?: return

        if (isLike) {
            updateBoardItem(
                item.copy(
                    likes = (count + 1).toString()
                )
            )
        } else {
            if (count > 0) {
                updateBoardItem(
                    item.copy(
                        likes = (count - 1).toString()
                    )
                )
            }
        }
    }

    fun getCommunityKey(): String {
        val ref = fireDatabase.getReference(PATH_COMMUNITY_BOARD)
        return ref.push().toString()
    }

}