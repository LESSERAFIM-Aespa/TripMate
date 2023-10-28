package kr.sparta.tripmate.ui.viewmodel.userproflie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveBoardFirebase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateCommuIsViewFromBoardRepo

class UserProfileBoardViewModel(
    private val getFirebaseBoardDataFromBoardRepo:
    GetFirebaseBoardDataFromBoardRepo,
    private val saveBoardFirebase: SaveBoardFirebase
) : ViewModel() {
    private val _userPage: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val userPage get() = _userPage

    fun getFirebaseBoardData() {
        Log.d("asdfasdfasdf", "뷰모델 데이터 호출은 되고있냐?")
        getFirebaseBoardDataFromBoardRepo.invoke(_userPage)
    }

    fun updateView(model:CommunityModelEntity){
        Log.d("asdfasdfasdf", "뷰모델 조회수 호출은 되고있냐?")
        val currentView = model.views?.toIntOrNull() ?:0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveBoardFirebase.invoke(model, _userPage)
        getFirebaseBoardData()
    }
}