package kr.sparta.tripmate.ui.viewmodel.userproflie

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseBoardDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseLikeDataUseCase
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class UserProfileBoardViewModel(
    private val getFirebaseBoardDataUseCase: GetFirebaseBoardDataUseCase,
    private val saveFirebaseBoardDataUseCase: SaveFirebaseBoardDataUseCase,
    private val saveFirebaseLikeDataUseCase: SaveFirebaseLikeDataUseCase
) : ViewModel() {
    private val _userPage: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val userPage get() = _userPage
    private val _likeKeyResults: MutableLiveData<List<KeyModelEntity?>> = MutableLiveData()
    val likeKeyResults get() = _likeKeyResults

    fun getFirebaseBoardData(uid: String) {
        Log.d("asdfasdfasdf", "뷰모델 데이터 호출은 되고있냐?")
        getFirebaseBoardDataUseCase.invoke(uid, _userPage, _likeKeyResults)
    }

    fun updateView(uid: String, model: CommunityModelEntity) {
        Log.d("asdfasdfasdf", "뷰모델 조회수 호출은 되고있냐?")
        val currentView = model.views?.toIntOrNull() ?: 0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveFirebaseBoardDataUseCase.invoke(model, _userPage)
        getFirebaseBoardData(uid)
    }

    fun updateCommuIsLike(model: CommunityModelEntity, context: Context, uid: String) {
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
        Log.d("rewq", "좋아요 ${model.commuIsLike}")
        saveFirebaseLikeDataUseCase.invoke(
            model,
            _userPage,
            _likeKeyResults,
            uid
        )
        getFirebaseBoardData(uid)
    }
}