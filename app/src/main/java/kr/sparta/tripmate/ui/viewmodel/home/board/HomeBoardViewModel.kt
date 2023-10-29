package kr.sparta.tripmate.ui.viewmodel.home.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveBoardFirebase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateCommuIsViewFromBoardRepo

class HomeBoardViewModel(private val getFirebaseBoardDataFromBoardRepo: GetFirebaseBoardDataFromBoardRepo,
private val saveBoardFirebase: SaveBoardFirebase
) :
    ViewModel
    () {
    private val _homeBoard: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val homeBoard get() = _homeBoard

    fun getHomeBoardData(uid: String) {
        getFirebaseBoardDataFromBoardRepo.invoke(_homeBoard)
    }
    fun updateBoardView(model:CommunityModelEntity){
        val currentView = model.views?.toIntOrNull() ?:0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveBoardFirebase.invoke(model, _homeBoard)
        getHomeBoardData(model.id)
    }
}