package kr.sparta.tripmate.ui.viewmodel.home.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseBoardDataUseCase

class HomeBoardViewModel(
    private val getFirebaseBoardDataUseCase: GetFirebaseBoardDataUseCase,
    private val saveFirebaseBoardDataUseCase: SaveFirebaseBoardDataUseCase
) :
    ViewModel
        () {
    private val _homeBoard: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val homeBoard get() = _homeBoard
    private val _likeKeyResults: MutableLiveData<List<KeyModelEntity?>> = MutableLiveData()
    val likeKeyResults get() = _likeKeyResults

    fun getHomeBoardData(uid: String) {
        getFirebaseBoardDataUseCase.invoke(uid, _homeBoard, _likeKeyResults)
    }

    fun updateBoardView(model: CommunityModelEntity) {
        val currentView = model.views?.toIntOrNull() ?: 0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveFirebaseBoardDataUseCase.invoke(model, _homeBoard)
        getHomeBoardData(model.id)
    }
}