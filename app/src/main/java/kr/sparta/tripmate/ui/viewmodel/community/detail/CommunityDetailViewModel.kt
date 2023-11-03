package kr.sparta.tripmate.ui.viewmodel.community.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.RemoveBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardScrapUseCase

class CommunityDetailViewModel(
    private val updateBoardScrapUseCase: UpdateBoardScrapUseCase,
    private val removeBoardUseCase: RemoveBoardUseCase
) : ViewModel() {
    // DetailPage 내부에서만 사용하는 현재 BoardScrap여부
    private val _isBoardScrap = MutableLiveData<Boolean>()
    val isBoardScrap: LiveData<Boolean> get() = _isBoardScrap

    /**
     * 작성자: 서정한
     * 내용: 게시물 스크랩버튼의 현재 토글여부를 업데이트합니다.
     * */
    fun updateIsBoardScrap(isBoardScrap: Boolean) {
        _isBoardScrap.value = isBoardScrap
    }

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
}