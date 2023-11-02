package kr.sparta.tripmate.ui.viewmodel.home.scrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.scrap.FirebaseBlogScrapRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBlogScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBlogScrapRepository
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetAllBlogScrapsUseCase

class HomeBlogScrapFactory: ViewModelProvider.Factory {
    private val repository: FirebaseBlogScrapRepository by lazy {
        FirebaseBlogScrapRepositoryImpl(FirebaseBlogScrapRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeBlogScrapViewModel::class.java)) {
            return HomeBlogScrapViewModel(GetAllBlogScrapsUseCase(repository)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}