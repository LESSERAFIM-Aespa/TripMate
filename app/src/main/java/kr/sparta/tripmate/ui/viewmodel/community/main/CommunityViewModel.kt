package kr.sparta.tripmate.ui.viewmodel.community.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveBoardFirebase
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommunityBaseData
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommuIsLike
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommuIsViewFromCommuRepo
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommuBoardKey
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class CommunityViewModel(
    private val updateCommunityBaseData: UpdateCommunityBaseData,
    private val updateCommuIsLike: UpdateCommuIsLike,
    private val updateCommuBoardKey: UpdateCommuBoardKey,
    private val getFirebaseBoardDataFromBoardRepo: GetFirebaseBoardDataFromBoardRepo,
    private val saveBoardFirebase: SaveBoardFirebase
) :
    ViewModel() {

    private val _communityResults: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val communityResults get() = _communityResults
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _keyModelList: MutableLiveData<List<KeyModelEntity?>> = MutableLiveData()
    val keyModelList get() = _keyModelList
    private val _boardKeyModelList: MutableLiveData<List<BoardKeyModelEntity?>> = MutableLiveData()
    val boardKeyModelList get() = _boardKeyModelList

    fun updateDataModelList() {
        getFirebaseBoardDataFromBoardRepo(_communityResults)
    }

    fun updateCommuIsLike(model: CommunityModelEntity,context: Context) =
        viewModelScope.launch {
//            kotlin.runCatching {
//                val uid = SharedPreferences.getUid(context)
//                updateCommuIsLike.invoke(
//                    model.toCommunity(),
//                    position,
//                    _dataModelList,
//                    _keyModelList,
//                    uid
//                )
//            }
        }

    fun updateBoardView(model:CommunityModelEntity){
        val currentView = model.views?.toIntOrNull() ?:0
        val newViews = currentView + 1
        model.views = newViews.toString()
        saveBoardFirebase.invoke(model, _communityResults)
        updateDataModelList()
    }
}