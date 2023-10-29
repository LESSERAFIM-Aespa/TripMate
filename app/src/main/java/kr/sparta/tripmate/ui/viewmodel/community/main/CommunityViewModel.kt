package kr.sparta.tripmate.ui.viewmodel.community.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseBoardDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseLikeDataUseCase
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class CommunityViewModel(
    private val saveFirebaseLikeDataUseCase: SaveFirebaseLikeDataUseCase,
    private val getFirebaseBoardDataUseCase: GetFirebaseBoardDataUseCase,
    private val saveFirebaseBoardDataUseCase: SaveFirebaseBoardDataUseCase
) :
    ViewModel() {

    private val _communityResults: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val communityResults get() = _communityResults
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _likeKeyResults: MutableLiveData<List<KeyModelEntity?>> = MutableLiveData()
    val likeKeyResults get() = _likeKeyResults
    private val _boardKeyResults: MutableLiveData<List<BoardKeyModelEntity?>> = MutableLiveData()
    val boardKeyModelList get() = _boardKeyResults

    fun updateDataModelList(uid: String) {

        getFirebaseBoardDataUseCase(uid, _communityResults, _likeKeyResults)

    }

    fun updateCommuIsLike(model: CommunityModelEntity, context: Context) {

        val currentLikes = model.likes?.toIntOrNull() ?: 0
        val newLikes = if (model.commuIsLike) {
            currentLikes + 1
        } else {
            if (currentLikes >= 1) {
                currentLikes - 1
            } else {
                currentLikes
            }
        }
        model.likes = newLikes.toString()

        saveFirebaseLikeDataUseCase.invoke(
            model,
            _communityResults,
            _likeKeyResults,
            SharedPreferences.getUid(context),
        )
        updateDataModelList(SharedPreferences.getUid(context))
    }

    fun updateBoardView(uid: String, model: CommunityModelEntity) {
        val currentView = model.views?.toIntOrNull() ?: 0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveFirebaseBoardDataUseCase.invoke(model, _communityResults)
        updateDataModelList(uid)
    }
}