package kr.sparta.tripmate.ui.viewmodel.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.usecase.GetFirebaseBoardData

class BoardViewModel(private val getFirebaseBoardData: GetFirebaseBoardData) : ViewModel() {
    private val _myPage : MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val myPage get() = _myPage

    fun getBoardData(uid:String){
        getFirebaseBoardData.invoke(uid,_myPage)
    }
}