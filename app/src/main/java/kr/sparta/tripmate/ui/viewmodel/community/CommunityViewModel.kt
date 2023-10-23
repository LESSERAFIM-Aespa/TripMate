package kr.sparta.tripmate.ui.viewmodel.community

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.data.model.community.KeyModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.GetFirebaseCommunityData
import kr.sparta.tripmate.domain.usecase.IsLikeFirebaseCommunityData
import kr.sparta.tripmate.domain.usecase.IsViewsFirebaseCommunityData
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class CommunityViewModel(
    private val getFirebaseCommunityData: GetFirebaseCommunityData,
    private val isLikeFirebaseCommunityData: IsLikeFirebaseCommunityData,
    private val isViewsFirebaseCommunityData: IsViewsFirebaseCommunityData
) :
    ViewModel() {

    private val _dataModelList: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val dataModelList get() = _dataModelList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _keyModelList: MutableLiveData<List<KeyModelEntity?>> = MutableLiveData()
    fun updateDataModelList(context: Context) = viewModelScope.launch {
        kotlin.runCatching {
            val uid = SharedPreferences.getUid(context)
            _isLoading.value = true
            getFirebaseCommunityData.invoke(uid, _dataModelList, _keyModelList)
            _isLoading.value = false
        }
    }

    fun updateCommuIsLike(model: CommunityModelEntity, position: Int, context: Context) =
        viewModelScope.launch {
            kotlin.runCatching {
                val uid = SharedPreferences.getUid(context)
                isLikeFirebaseCommunityData.invoke(
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
}