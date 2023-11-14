package kr.sparta.tripmate.ui.viewmodel.mypage.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase

class MyPageBoardViewModel(
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val updateBoardViewsUseCase: UpdateBoardViewsUseCase,
    private val updateBoardLikeUseCase: UpdateBoardLikeUseCase,
    private val getUidUseCase: GetUidUseCase,
) :
    ViewModel() {
    private val _myBoards: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val myBoards get() = _myBoards

    /**
     * 작성자: 서정한
     * 내용: 모든 게시글 목록을 가져옵니다.
     * */
    fun getAllMyBoards(uid: String) = CoroutineScope(Dispatchers.Main).launch {
        getAllBoardsUseCase().collect() { boards ->
            val myBoard = boards.filter { it?.userid == uid }

            _myBoards.value = myBoard.toEntity()
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 조회수 증가
     * */
    fun updateBoardView(model: CommunityEntity) {
        updateBoardViewsUseCase(model)
    }

    /**
     * 작성자: 서정한
     * 내용: 게시글 좋아요 업데이트
     * */
    fun updateBoardLikes(uid: String, key: String) {
        updateBoardLikeUseCase(uid, key)
    }

    fun getUid(): String = getUidUseCase()
}