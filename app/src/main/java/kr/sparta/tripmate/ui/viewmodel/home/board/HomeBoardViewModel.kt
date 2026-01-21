package kr.sparta.tripmate.ui.viewmodel.home.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeBoardViewModel @Inject constructor(
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val updateBoardViewsUseCase: UpdateBoardViewsUseCase,
) :
    ViewModel
        () {
    private val _boards: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val boards get() = _boards

    /**
     * 작성자: 서정한
     * 내용: 모든 게시글목록을 가져옵니다.
     * */
    fun getAllBoards() = viewModelScope.launch {
        getAllBoardsUseCase().collect() {
            _boards.value = it.toEntity()
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 게시글 조회수 증가
     * */
    fun updateBoardView(model: CommunityEntity) = updateBoardViewsUseCase(model)
}