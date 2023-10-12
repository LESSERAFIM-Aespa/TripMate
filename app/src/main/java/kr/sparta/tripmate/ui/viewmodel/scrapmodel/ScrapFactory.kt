package kr.sparta.tripmate.ui.viewmodel.scrapmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.api.naver.NetWorkInterface

class ScrapFactory(private val apiService: NetWorkInterface): ViewModelProvider.Factory {
    override fun<T : ViewModel> create(modelClass:Class<T>):T{
        return ScrapViewModel(apiService) as T
    }
}