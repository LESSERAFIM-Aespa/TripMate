package kr.sparta.tripmate.ui.viewmodel.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.ui.community.main.CommunityModel

class CommunityViewModel : ViewModel() {
    private val _dataModelList = MutableLiveData<MutableList<CommunityModel>>()
    val dataModelList: LiveData<MutableList<CommunityModel>> get() = _dataModelList

    fun updateDataModelList(newData: MutableList<CommunityModel>) {
        _dataModelList.value = newData
    }
}