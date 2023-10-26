package kr.sparta.tripmate.ui.viewmodel.community

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
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommunityBaseData
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommuIsLike
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommuIsViewFromCommuRepo
import kr.sparta.tripmate.domain.usecase.firebasecommunityrepository.UpdateCommuBoardKey
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class CommunityViewModel(
    private val updateCommunityBaseData: UpdateCommunityBaseData,
    private val updateCommuIsLike: UpdateCommuIsLike,
    private val isViewsFirebaseCommunityData: UpdateCommuIsViewFromCommuRepo,
    private val updateCommuBoardKey: UpdateCommuBoardKey
) :
    ViewModel() {

    private val _dataModelList: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val dataModelList get() = _dataModelList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _keyModelList: MutableLiveData<List<KeyModelEntity?>> = MutableLiveData()
    private val _boardKeyModelList: MutableLiveData<List<BoardKeyModelEntity?>> = MutableLiveData()
    fun updateDataModelList(context: Context) = viewModelScope.launch {
        kotlin.runCatching {
            val uid = SharedPreferences.getUid(context)
            _isLoading.value = true
            updateCommunityBaseData.invoke(uid, _dataModelList, _keyModelList, _boardKeyModelList)
            _isLoading.value = false
        }
    }

    fun updateCommuIsLike(model: CommunityModelEntity, position: Int, context: Context) =
        viewModelScope.launch {
            kotlin.runCatching {
                val uid = SharedPreferences.getUid(context)
                updateCommuIsLike.invoke(
                    model.toCommunity(),
                    position,
                    _dataModelList,
                    _keyModelList,
                    uid
                )
            }
        }

    fun updateCommuView(model: CommunityModelEntity, position: Int) = viewModelScope.launch {
        kotlin.runCatching {
            isViewsFirebaseCommunityData.invoke(model.toCommunity(), position, _dataModelList)
        }
    }

    fun updateCommuBoard(model: CommunityModelEntity, position: Int, context: Context) =
        viewModelScope
            .launch {
                kotlin.runCatching {
                    val uid = SharedPreferences.getUid(context)
                    updateCommuBoardKey.invoke(
                        model.toCommunity(), position, _dataModelList,
                        _boardKeyModelList, uid, context
                    )
                    Log.d("TripMates", "뷰모델 :${model.boardIsLike}")
                }
            }
}