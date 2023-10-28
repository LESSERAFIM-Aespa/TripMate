package kr.sparta.tripmate.ui.viewmodel.mypage.board

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.usecase.community.board.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.GetMyBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemLikesUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemViewsUseCase
import kr.sparta.tripmate.domain.usecase.community.like.UpdateIsLikeUseCase
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences
import kotlin.coroutines.resume

class BoardViewModel(
    private val getMyBoardsUseCase: GetMyBoardsUseCase,
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val updateBoardItemViewsUseCase: UpdateBoardItemViewsUseCase,
    private val updateBoardItemLikesUseCase: UpdateBoardItemLikesUseCase,
    private val updateIsLikeUseCase: UpdateIsLikeUseCase,
) :
    ViewModel() {
    private val _myBoards: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val myBoards get() = _myBoards

    /**
     * 작성자: 서정한
     * 내용: 내가 쓴 모든글 가져오기
     * */
    suspend fun getAllMyBoards(uid: String) = viewModelScope.launch {
        val list = getMyBoardsUseCase.invoke(uid)

        _myBoards.value = list
    }

    /**
     * 작성자: 서정한
     * 내용: 내가 쓴글의 조회수 증가
     * */
    suspend fun viewMyPageBoardData(model: CommunityEntity) = viewModelScope.launch {
        // 조회수 증가 업데이트
        updateBoardItemViewsUseCase.invoke(model)

        _myBoards.value = getAllBoardsUseCase.invoke()
    }

    suspend fun updateLikes(context: Context, model: CommunityEntity) = viewModelScope.launch {
        val uid = SharedPreferences.getUid(context)
        // 좋아요 추가
        val isLike = updateIsLikeUseCase.invoke(
            uid,
            model.copy(
                isLike = !model.isLike
            )
        )

        // 좋아요 수 증가
        updateBoardItemLikesUseCase.invoke(model, isLike)

        val list = getMyBoardsUseCase.invoke(uid).toMutableList()

        for (i in list.indices) {
            if (list[i].key == model.key) {
                list[i] = list[i].copy(
                    isLike = isLike
                )
            }
        }

        _myBoards.value = list
    }
}