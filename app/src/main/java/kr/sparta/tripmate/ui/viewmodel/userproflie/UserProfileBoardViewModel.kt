package kr.sparta.tripmate.ui.viewmodel.userproflie

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.community.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.AddBoardUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardLikeUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase

class UserProfileBoardViewModel(
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val updateBoardViewsUseCase: UpdateBoardViewsUseCase,
    private val updateBoardLikeUseCase: UpdateBoardLikeUseCase,
) : ViewModel() {
    private val _userPage: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val userPage get() = _userPage

    fun getFirebaseBoardData(uid: String) = CoroutineScope(Dispatchers.Main).launch {
        getAllBoardsUseCase().collect() {userData ->
            val filterData = userData.filter { it?.id == uid }
            _userPage.value = filterData.toEntity()
        }
    }

    fun updateView(model: CommunityEntity) {
        // 조회수 업데이트
        updateBoardViewsUseCase.invoke(model)
    }

    fun updateCommuIsLike(uid: String, key: String) = viewModelScope.launch {
        updateBoardLikeUseCase(uid, key)
    }
}
