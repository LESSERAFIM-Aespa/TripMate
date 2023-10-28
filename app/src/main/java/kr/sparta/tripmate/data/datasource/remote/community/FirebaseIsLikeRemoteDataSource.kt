package kr.sparta.tripmate.data.datasource.remote.community

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.sparta.tripmate.data.model.community.CommunityModel
import kotlin.coroutines.resume

/**
 * 작성자: 서정한
 * 내용: 좋아요 목록 가져오기
 * */
class FirebaseIsLikeRemoteDataSource {
    private val fireDatabase = Firebase.database

    /**
     * 작성자: 서정한
     * 내용: id가 가진 모든 좋아요 목록 가져오기
     * */
    suspend fun getAllIsLikesById(uid: String):List<CommunityModel> {
        val result = suspendCancellableCoroutine<List<CommunityModel>> { continuation ->
            val ref = fireDatabase.getReference("Bookmark").child("IsLikeData").child(uid)
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
     * 내용: id가 가진 좋아요 목록중 선택한 좋아요 가져오기
     * */
    suspend fun getIsLikeByKey(uid: String, key: String): CommunityModel? {
        val list = getAllIsLikesById(uid)

        return list.find { it.key == key }
    }

    /**
     * 작성자: 서정한
     * 내용: id에 새 좋아요 게시글 추가
     * */
    suspend fun addIsLikeByKey(uid: String, item: CommunityModel) {
        val ref = fireDatabase.getReference("Bookmark").child("IsLikeData").child(uid)
        val list = getAllIsLikesById(uid).toMutableList()
        list.add(item)

        ref.setValue(list)
    }

    /**
     * 작성자: 서정한
     * 내용: 좋아요 게시글 업데이트. 없으면 추가 있으면 제거
     * */
    suspend fun updateIsLikeByKey(uid: String, item: CommunityModel):Boolean {
        val list = getAllIsLikesById(uid)
        val isExist = list.firstOrNull { it.key == item.key }
        // 존재하면 제거
        if(isExist != null) {
            removedIsLikeByKey(uid, item.key.toString())
            return false
        }
        // 없을경우 추가
        addIsLikeByKey(uid, item)
        return true
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 좋아요 삭제
     * */
    suspend fun removedIsLikeByKey(uid: String, key: String) {
        val ref = fireDatabase.getReference("Bookmark").child("IsLikeData").child(uid)
        val list = getAllIsLikesById(uid).toMutableList()
        list.find { it.key == key }?.let{
            list.remove(it)
        }

        ref.setValue(list)
    }
}