package kr.sparta.tripmate.ui.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveBoardFirebase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateCommuIsViewFromBoardRepo

class BoardViewModel(
    private val getFirebaseBoardDataFromBoardRepo: GetFirebaseBoardDataFromBoardRepo,
    private val saveBoardFirebase: SaveBoardFirebase
) :
    ViewModel() {
    private val _myPage: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val myPage get() = _myPage

    fun getBoardData() {
        getFirebaseBoardDataFromBoardRepo.invoke(_myPage)
    }
    fun updateBoardView(model:CommunityModelEntity){
        val currentView = model.views?.toIntOrNull() ?:0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveBoardFirebase.invoke(model, _myPage)
        getBoardData()
    }
}