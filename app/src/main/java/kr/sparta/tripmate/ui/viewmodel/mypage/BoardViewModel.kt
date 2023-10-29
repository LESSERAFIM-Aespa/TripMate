package kr.sparta.tripmate.ui.viewmodel.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseBoardDataUseCase

class BoardViewModel(
    private val getFirebaseBoardDataUseCase: GetFirebaseBoardDataUseCase,
    private val saveFirebaseBoardDataUseCase: SaveFirebaseBoardDataUseCase
) :
    ViewModel() {
    private val _myPage: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val myPage get() = _myPage

    private val _likeKeyResults: MutableLiveData<List<KeyModelEntity?>> = MutableLiveData()
    val likeKeyResults get() = _likeKeyResults

    fun getBoardData(uid: String) {
        getFirebaseBoardDataUseCase.invoke(uid, _myPage, _likeKeyResults)
    }

    fun updateBoardView(uid: String, model: CommunityModelEntity) {
        val currentView = model.views?.toIntOrNull() ?: 0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveFirebaseBoardDataUseCase.invoke(model, _myPage)
        getBoardData(uid)
    }
}