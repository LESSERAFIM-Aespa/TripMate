package kr.sparta.tripmate.ui.viewmodel.community.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardScrapUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase

class CommunityViewModel(
    private val updateBoardLikeUseCase: UpdateBoardLikeUseCase,
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val updateBoardViewsUseCase: UpdateBoardViewsUseCase,
    private val updateBoardScrapUseCase: UpdateBoardScrapUseCase
) :
    ViewModel() {

    // 게시글 목록
    private val _boards: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val boards get() = _boards

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /**
     * 작성자 : 박성수
     * 내용 : RDB 커뮤니티데이터 불러옵니다.
     */
    suspend fun getAllBoards() {
        Log.d("tripmatessss", "getAllBoards이 호출되고있냐?")
        getAllBoardsUseCase.invoke().collect() {
            _boards.value = it.toEntity()
        }
    }

    /**
     * 작성자 : 박성수
     * 내용 : 좋아요 누른 아이템 저장
     */
    fun updateCommuIsLike(
        uid: String,
        model: CommunityEntity,
    ) = viewModelScope.launch {
        updateBoardLikeUseCase(
            uid = uid,
            key = model.key ?: "",
        )
    }

    /**
     * 작성자 : 박성수
     * 내용 : 조회수가 올라가고, 저장됩니다.
     */
    fun updateBoardView(model: CommunityEntity) {
        updateBoardViewsUseCase.invoke(model)
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 게시글을 스크랩목록에 추가합니다.
     * */
    fun addBoardScrap(uid: String, key: String) = updateBoardScrapUseCase(uid, key)
}