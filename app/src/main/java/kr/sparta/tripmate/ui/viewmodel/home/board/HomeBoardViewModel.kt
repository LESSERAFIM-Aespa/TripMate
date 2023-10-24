package kr.sparta.tripmate.ui.viewmodel.home.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.GetFirebaseBoardData
import kr.sparta.tripmate.domain.usecase.IsFirebaseBoardViews

class HomeBoardViewModel(private val getFirebaseBoardData: GetFirebaseBoardData, private val
isFirebaseBoardViews: IsFirebaseBoardViews
) :
    ViewModel
    () {
    private val _homeBoard: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val homeBoard get() = _homeBoard

    fun getHomeBoardData(uid: String) {
        getFirebaseBoardData.invoke(uid, _homeBoard)
    }
    fun viewHomeBoardData(model:CommunityModelEntity,position:Int){
        isFirebaseBoardViews.invoke(model.toCommunity(), position,_homeBoard)
    }
}