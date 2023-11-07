package kr.sparta.tripmate.ui.viewmodel.community.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.RemoveBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardScrapUseCase

class CommunityDetailViewModel(
    private val updateBoardScrapUseCase: UpdateBoardScrapUseCase,
    private val removeBoardUseCase: RemoveBoardUseCase,
    private val updateBoardLikeUseCase: UpdateBoardLikeUseCase,
    private val getAllBoardsUseCase: GetAllBoardsUseCase
) : ViewModel() {
    // DetailPage 내부에서만 사용하는 현재 BoardScrap여부
    private val _isBoardScrap = MutableLiveData<Boolean>()
    /**
     * 작성자 : 박성수
     * 내용 : 게시물 목록
     */
    private val _boards: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val boards get() = _boards

    /**
     * 작성자: 서정한
     * 내용: 선택한 게시물을 스크랩목록에 업데이트합니다.
     * */
    fun updateBoardScrap(uid: String, key: String) = updateBoardScrapUseCase(uid, key)

    /**
     * 작성자: 서정한
     * 내용: 선택한 게시글을 삭제합니다.
     * */
    fun removeBoard(key: String) = removeBoardUseCase(key)

    /**
     * 작성자 : 박성수
     * 내용 : 좋아요 버튼 기능을 담당합니다.
     */
    fun updateBoardLike(uid: String, model: CommunityEntity) = viewModelScope.launch {
        updateBoardLikeUseCase(uid, model.key?:"")
    }
    suspend fun getAllBoards(){
        getAllBoardsUseCase.invoke().collect() {
            _boards.value = it.toEntity()
        }
    }

}