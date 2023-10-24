package kr.sparta.tripmate.ui.viewmodel.home.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.usecase.GetFirebaseBoardData

class HomeBoardViewModel(private val getFirebaseBoardData: GetFirebaseBoardData) : ViewModel() {
    private val _homeBoard: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val homeBoard get() = _homeBoard

    fun getHomeBoardData(uid: String) {
        getFirebaseBoardData.invoke(uid, _homeBoard)
    }
}