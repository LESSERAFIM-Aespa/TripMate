package kr.sparta.tripmate.ui.viewmodel.scrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.api.naver.NaverNetWorkClient
import kr.sparta.tripmate.data.repository.ScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.ScrapRepository
import kr.sparta.tripmate.domain.usecase.GetSearchBlogUseCase

class ScrapFactory: ViewModelProvider.Factory {
    private val repository: ScrapRepository = ScrapRepositoryImpl(
        NaverNetWorkClient.apiService
    )
    override fun<T : ViewModel> create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(ScrapViewModel::class.java)) {
            return ScrapViewModel(
                GetSearchBlogUseCase(repository)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}