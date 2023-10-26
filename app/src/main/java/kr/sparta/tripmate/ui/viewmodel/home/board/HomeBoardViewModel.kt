package kr.sparta.tripmate.ui.viewmodel.home.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateCommuIsViewFromBoardRepo

class HomeBoardViewModel(private val getFirebaseBoardDataFromBoardRepo: GetFirebaseBoardDataFromBoardRepo, private val
updateCommuIsViewFromBoardRepo: UpdateCommuIsViewFromBoardRepo
) :
    ViewModel
    () {
    private val _homeBoard: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val homeBoard get() = _homeBoard

    fun getHomeBoardData(uid: String) {
        getFirebaseBoardDataFromBoardRepo.invoke(uid, _homeBoard)
    }
    fun viewHomeBoardData(model:CommunityModelEntity,position:Int){
        updateCommuIsViewFromBoardRepo.invoke(model.toCommunity(), position,_homeBoard)
    }
}