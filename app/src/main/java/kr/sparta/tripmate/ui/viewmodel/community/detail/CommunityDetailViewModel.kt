package kr.sparta.tripmate.ui.viewmodel.community.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.RemoveBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardScrapUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val updateBoardScrapUseCase: UpdateBoardScrapUseCase,
    private val removeBoardUseCase: RemoveBoardUseCase,
    private val updateBoardLikeUseCase: UpdateBoardLikeUseCase,
    private val getBoardUseCase: GetBoardUseCase,
    private val getUidUseCase: GetUidUseCase
) : ViewModel() {

    /**
     * 작성자 : 박성수
     * 내용 : 게시물 목록
     */
    private val _boards: MutableLiveData<CommunityEntity?> = MutableLiveData()
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
        updateBoardLikeUseCase(uid, model.key ?: "")
    }

    suspend fun getBoard(key: String) {
        getBoardUseCase(key).collect(){
            _boards.value = it?.toEntity()
        }
    }

    fun getUid() : String = getUidUseCase()

}