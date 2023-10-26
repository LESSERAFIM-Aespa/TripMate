package kr.sparta.tripmate.ui.viewmodel.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.GetFirebaseBoardData
import kr.sparta.tripmate.domain.usecase.IsViewsFirebaseBoardData

class BoardViewModel(
    private val getFirebaseBoardData: GetFirebaseBoardData,
    private val isViewsFirebaseBoardData: IsViewsFirebaseBoardData
) :
    ViewModel() {
    private val _myPage: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val myPage get() = _myPage

    fun getBoardData(uid: String) {
        getFirebaseBoardData.invoke(uid, _myPage)
    }

    fun viewMyPageBoardData(model: CommunityModelEntity, position: Int) {
        isViewsFirebaseBoardData.invoke(model.toCommunity(), position, _myPage)
    }
}