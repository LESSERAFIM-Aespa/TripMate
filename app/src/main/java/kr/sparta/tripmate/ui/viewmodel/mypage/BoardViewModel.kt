package kr.sparta.tripmate.ui.viewmodel.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateCommuIsViewFromBoardRepo

class BoardViewModel(
    private val getFirebaseBoardDataFromBoardRepo: GetFirebaseBoardDataFromBoardRepo,
    private val updateCommuIsViewFromBoardRepo: UpdateCommuIsViewFromBoardRepo
) :
    ViewModel() {
    private val _myPage: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val myPage get() = _myPage

    fun getBoardData(uid: String) {
        getFirebaseBoardDataFromBoardRepo.invoke(uid, _myPage)
    }

    fun viewMyPageBoardData(model: CommunityModelEntity, position: Int) {
        updateCommuIsViewFromBoardRepo.invoke(model.toCommunity(), position, _myPage)
    }
}