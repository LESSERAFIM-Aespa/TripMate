package kr.sparta.tripmate.ui.viewmodel.userproflie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseBoardDataUseCase

class UserProfileBoardViewModel(
    private val getFirebaseBoardDataUseCase:
    GetFirebaseBoardDataUseCase,
    private val saveFirebaseBoardDataUseCase: SaveFirebaseBoardDataUseCase
) : ViewModel() {
    private val _userPage: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val userPage get() = _userPage
    private val _likeKeyResults: MutableLiveData<List<KeyModelEntity?>> = MutableLiveData()
    val likeKeyResults get() = _likeKeyResults

    fun getFirebaseBoardData(uid:String) {
        Log.d("asdfasdfasdf", "뷰모델 데이터 호출은 되고있냐?")
        getFirebaseBoardDataUseCase.invoke(uid,_userPage,_likeKeyResults)
    }

    fun updateView(uid:String,model:CommunityModelEntity){
        Log.d("asdfasdfasdf", "뷰모델 조회수 호출은 되고있냐?")
        val currentView = model.views?.toIntOrNull() ?:0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveFirebaseBoardDataUseCase.invoke(model, _userPage)
        getFirebaseBoardData(uid)
    }
}