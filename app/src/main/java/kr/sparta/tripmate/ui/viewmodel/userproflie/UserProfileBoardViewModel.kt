package kr.sparta.tripmate.ui.viewmodel.userproflie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.usecase.community.board.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.community.board.UpdateBoardItemViewsUseCase

class UserProfileBoardViewModel(
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val updateBoardItemViewsUseCase: UpdateBoardItemViewsUseCase,
) : ViewModel() {
    private val _userPage: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val userPage get() = _userPage

    suspend fun getFirebaseBoardData() {
        Log.d("asdfasdfasdf", "뷰모델 데이터 호출은 되고있냐?")
        _userPage.value = getAllBoardsUseCase.invoke()
    }

    suspend fun updateView(model: CommunityEntity) {
        updateBoardItemViewsUseCase.invoke(model)

        _userPage.value = getAllBoardsUseCase.invoke()
    }
}