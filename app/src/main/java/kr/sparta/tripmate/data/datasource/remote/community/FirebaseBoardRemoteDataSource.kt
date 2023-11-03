package kr.sparta.tripmate.data.datasource.remote.community

import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity

/**
 * 작성자: 서정한
 * 내용: Firebase RealtimeDatabase에서 필요한 자료를
 * 요청하고 응답받는 DataSource Class
 * */
class FirebaseBoardRemoteDataSource {
    private val REFERENCE_COMMUNITY_DATA = "CommunityData"
    private fun getReference() = Firebase.database.getReference(REFERENCE_COMMUNITY_DATA)

    /**
     * 작성자: 서정한
     * 내용: 게시글 업로드시 사용할 키를 반환함.
     * */
    fun getKey(): String {
        return getReference().push().toString()
    }

    /**
     * 작성자: 서정한
     * 내용: 커뮤니티 게시글 목록 가져오기
     * */
    fun getAllBoards(): Flow<List<CommunityModel?>> {
        val ref = getReference()
        return ref.snapshots.map { snapshot ->
            snapshot.children.mapNotNull {
                it.getValue<CommunityModel>()
            }
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 커뮤니티 게시글 가져오기
     * */
    private fun getBoard(key: String): Flow<CommunityModel?> {
        val ref = getReference().child(key)

        return ref.snapshots.mapNotNull {
            it.getValue<CommunityModel>()
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 커뮤니티에 게시글 추가
     * */
    fun addBoard(item: CommunityModel) {
        val ref = getReference()
        ref.get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull {
                it.getValue<CommunityModel>()
            }.toMutableList()

            // 새로운 글 업로드
            list.add(item)
            ref.setValue(list)

        }
    }

    /**
     * 작성자: 서정한
     * 내용: 커뮤니티 게시글 목록 업데이트
     * */
    fun updateBoard(
        item: CommunityModel
    ) {
        val ref = getReference()
        ref.get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull {
                it.getValue<CommunityModel>()
            }.toMutableList()

            list.forEachIndexed { index, communityModel ->
                if (communityModel.key == item.key) {
                    list[index] = item.copy(
                        views = item.views?.plus(1)
                    )
                    return@forEachIndexed
                }
            }
            ref.setValue(list)
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 커뮤니티 게시글 삭제
     * */
    fun removeBoard(key: String) {
        val ref = getReference()
        ref.get().addOnSuccessListener {snapshot ->
            val boards = snapshot.children.map {
                it.getValue(CommunityModel::class.java)
            }.toMutableList()

            val removeItem = boards.find { it?.key == key } ?: return@addOnSuccessListener
            boards.remove(removeItem)

            ref.setValue(boards)
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 게시글의 좋아요 클릭시 내 좋아요 클릭목록 업데이트.
     * */
    fun updateBoardLike(uid: String, key: String) {
        val ref = getReference()
        ref.get().addOnSuccessListener { snapshot ->
            // 내가 좋아요누른 게시판Key 가져오기
            val boards = snapshot.children.map {
                it.getValue(CommunityModel::class.java)
            }.toMutableList()

            // key와 일치하는 게시글 불러오기
            val model = boards.find { it?.key == key } ?: return@addOnSuccessListener
            val index = boards.indexOf(model)

            // 게시글의 좋아요 유저에 좋아요 클릭한 user추가
            val currentLikeUsers = model.likeUsers.toMutableList()
            val isLike = currentLikeUsers.find { it == uid }

            if (isLike.isNullOrEmpty()) {
                addBoardLike(boards, index, model, uid)
            } else {
                removeBoardLike(boards, index, model, uid)
            }
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 내가 클릭한 게시글을 내 좋아요 목록에 추가
     * 그리고 좋아요수 +1
     * */
    private fun addBoardLike(
        boards: MutableList<CommunityModel?>,
        index: Int,
        model: CommunityModel,
        uid: String
    ) {
        val ref = getReference()
        // 게시글의 좋아요 유저에 좋아요 클릭한 user추가
        val currentLikeUsers = model.likeUsers.toMutableList()
        currentLikeUsers.add(uid)

        boards[index] = model.copy(
            likes = boards[index]?.likes?.plus(1),
            likeUsers = currentLikeUsers
        )

        ref.setValue(boards)
    }

    /**
     * 작성자: 서정한
     * 내용: 내 좋아요목록에서 해당 게시글 삭제
     * 게시글이 삭제될때만 동작함.
     * 그리고 좋아요 수 -1
     * */
    private fun removeBoardLike(
        boards: MutableList<CommunityModel?>,
        index: Int,
        model: CommunityModel,
        uid: String,
    ) {
        val ref = getReference()
        val currentLikeUsers = model.likeUsers.toMutableList()
        currentLikeUsers.remove(uid)

        boards[index] = model.copy(
            likes = boards[index]?.likes?.minus(1),
            likeUsers = currentLikeUsers
        )

        ref.setValue(boards)
    }

    /**
     * 작성자: 서정한
     * 내용: 내가 스크랩한 게시글을 업데이트합니다.
     * */
    fun updateScrapBoards(uid: String, key: String) {
        val ref = getReference()
        ref.get().addOnSuccessListener { snapshot ->
            // 게시판목록 가져오기
            val boards = snapshot.children.map {
                it.getValue(CommunityModel::class.java)
            }.toMutableList()

            // key와 일치하는 게시글 불러오기
            val model = boards.find { it?.key == key } ?: return@addOnSuccessListener
            val index = boards.indexOf(model)

            // 게시글의 스크랩목록에 내 uid 추가 혹은 제거
            val currentScrapBoards = model.scrapUsers.toMutableList()
            val isScrap = currentScrapBoards.find { it == uid }

            if (isScrap.isNullOrEmpty()) {
                addScrapBoard(boards, index, model, uid)
            } else {
                removeScrapBoard(boards, index, model, uid)
            }
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 내가 클릭한 게시글을 내 게시글 스크랩 목록에 추가
     * */
    private fun addScrapBoard(
        boards: MutableList<CommunityModel?>,
        index: Int,
        model: CommunityModel,
        uid: String
    ) {
        val ref = getReference()
        // 게시글의 스크랩목록에 내 uid 추가 혹은 제거
        val currentScrapBoards = model.scrapUsers.toMutableList()
        currentScrapBoards.add(uid)

        boards[index] = model.copy(
            scrapUsers = currentScrapBoards
        )

        ref.setValue(boards)
    }

    /**
     * 작성자: 서정한
     * 내용: 내 게시글 스크랩 목록에서 해당 게시글 삭제
     * 게시글이 삭제될때만 동작함.
     * */
    private fun removeScrapBoard(
        boards: MutableList<CommunityModel?>,
        index: Int,
        model: CommunityModel,
        uid: String,
    ) {
        val ref = getReference()
        val currentScrapBoards = model.scrapUsers.toMutableList()
        currentScrapBoards.remove(uid)

        boards[index] = model.copy(
            scrapUsers = currentScrapBoards
        )

        ref.setValue(boards)
    }
}