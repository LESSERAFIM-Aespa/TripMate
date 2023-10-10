package kr.sparta.tripmate.viewmodel.gourmet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.api.NetWorkInterface

class GourmetFactory(private val apiService:NetWorkInterface): ViewModelProvider.Factory {
    override fun<T : ViewModel> create(modelClass:Class<T>):T{
        return GourmetViewModel(apiService) as T
    }
}