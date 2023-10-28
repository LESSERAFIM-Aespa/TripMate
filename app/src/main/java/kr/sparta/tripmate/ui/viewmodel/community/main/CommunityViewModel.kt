package kr.sparta.tripmate.ui.viewmodel.community.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.usecase.community.board.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemLikesUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemViewsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.GetAllBookmarkedBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.board.UpdateBookmarkedBoardUseCase
import kr.sparta.tripmate.domain.usecase.community.like.UpdateIsLikeUseCase
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class CommunityViewModel(
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val updateBoardItemViewsUseCase: UpdateBoardItemViewsUseCase,
    private val updateBoardItemLikesUseCase: UpdateBoardItemLikesUseCase,
    private val updateBookmarkedBoardUseCase: UpdateBookmarkedBoardUseCase,
    private val updateIsLikeUseCase: UpdateIsLikeUseCase,
) :
    ViewModel() {

    private val _boardList: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val boardList get() = _boardList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 게시판 전체목록 불러오기
    suspend fun getAllBoards(context: Context) {
        val list = getAllBoardsUseCase.invoke()
        _boardList.value = list
    }

    // 좋아요 업데이트
    suspend fun updateCommuIsLike(model: CommunityEntity, context: Context) =
        viewModelScope.launch {
            val uid = SharedPreferences.getUid(context)
            // 좋아요목록 업데이트
            val isLike = updateIsLikeUseCase.invoke(
                uid,
                item = model.copy(
                    isLike = !model.isLike,
                )
            )

            // 좋아요 수 증가
            updateBoardItemLikesUseCase.invoke(model, isLike)

            val list = getAllBoardsUseCase.invoke().toMutableList()
            for (i in list.indices) {
                if (list[i].key == model.key) {
                    list[i] = list[i].copy(
                        isLike = isLike
                    )
                }
            }

            // 게시판 최신화
            _boardList.value = list
        }

    // 조회수 업데이트
    suspend fun updateCommuView(model: CommunityEntity) = viewModelScope.launch {
        updateBoardItemViewsUseCase.invoke(model)

        _boardList.value = getAllBoardsUseCase.invoke()
    }

    // 게시글 북마크
    suspend fun addBoardBookmark(model: CommunityEntity, context: Context) = viewModelScope.launch {
        val uid = SharedPreferences.getUid(context)
        updateBookmarkedBoardUseCase.invoke(uid, model)
    }
}
