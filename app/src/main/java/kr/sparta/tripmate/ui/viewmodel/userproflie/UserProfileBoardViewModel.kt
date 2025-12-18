package kr.sparta.tripmate.ui.viewmodel.userproflie

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
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidFromUserUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileBoardViewModel @Inject constructor(
    private val getAllBoardsUseCase: GetAllBoardsUseCase,
    private val updateBoardViewsUseCase: UpdateBoardViewsUseCase,
    private val updateBoardLikeUseCase: UpdateBoardLikeUseCase,
    private val getUidUseCase: GetUidUseCase,
    private val getUidFromUserUseCase: GetUidFromUserUseCase,
) : ViewModel() {
    private val _userPage: MutableLiveData<List<CommunityEntity?>> = MutableLiveData()
    val userPage get() = _userPage

    fun getFirebaseBoardData(uid: String) = CoroutineScope(Dispatchers.Main).launch {
        getAllBoardsUseCase().collect() { userData ->
            val filterData = userData.filter { it?.userid == uid }
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

    fun getUid(): String = getUidUseCase()
    fun getUidFromUser() : String = getUidFromUserUseCase()
}
