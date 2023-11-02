package kr.sparta.tripmate.ui.viewmodel.scrap.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.scrap.FirebaseBlogScrapRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBlogScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBlogScrapRepository
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.UpdateBlogScrapUseCase

class ScrapDetailFactory : ViewModelProvider.Factory {
    private val repository: FirebaseBlogScrapRepository by lazy {
        FirebaseBlogScrapRepositoryImpl(FirebaseBlogScrapRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScrapDetailViewModel::class.java)) {
            return ScrapDetailViewModel(
                UpdateBlogScrapUseCase(repository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}