package kr.sparta.tripmate.ui.viewmodel.community.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseBoardDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseBookMarkDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveFirebaseLikeDataUseCase
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class CommunityViewModel(
    private val saveFirebaseLikeDataUseCase: SaveFirebaseLikeDataUseCase,
    private val getFirebaseBoardDataUseCase: GetFirebaseBoardDataUseCase,
    private val saveFirebaseBoardDataUseCase: SaveFirebaseBoardDataUseCase,
    private val saveFirebaseBookMarkDataUseCase: SaveFirebaseBookMarkDataUseCase
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

    /**
     * 작성자 : 박성수
     * 내용 : RDB 커뮤니티데이터 불러옵니다.
     */
    fun updateDataModelList(uid: String) {

        getFirebaseBoardDataUseCase(uid, _communityResults, _likeKeyResults)
    }

    /**
     * 작성자 : 박성수
     * 내용 : 좋아요 누른 아이템 저장
     */
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

    /**
     * 작성자 : 박성수
     * 내용 : 조회수가 올라가고, 저장됩니다.
     */
    fun updateBoardView(uid: String, model: CommunityModelEntity) {
        val currentView = model.views?.toIntOrNull() ?: 0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveFirebaseBoardDataUseCase.invoke(model, _communityResults)
        updateDataModelList(uid)
    }

    fun saveBookMarkData(
        model: CommunityModelEntity, uid: String, context: Context
    ) {
        saveFirebaseBookMarkDataUseCase(model, uid, context, _communityResults, _boardKeyResults)
    }
}