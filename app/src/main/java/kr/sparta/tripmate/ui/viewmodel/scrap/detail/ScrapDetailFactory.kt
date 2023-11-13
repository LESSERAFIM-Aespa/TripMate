package kr.sparta.tripmate.ui.viewmodel.scrap.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.local.sharedpreference.SharedPreferencesLocalDataSource
import kr.sparta.tripmate.data.datasource.remote.community.scrap.FirebaseBlogScrapRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBlogScrapRepositoryImpl
import kr.sparta.tripmate.data.repository.sharedpreference.SharedPreferenceReopositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBlogScrapRepository
import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.UpdateBlogScrapUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import kr.sparta.tripmate.util.TripMateApp

class ScrapDetailFactory : ViewModelProvider.Factory {
    private val firebaseBlogScrapRepository: FirebaseBlogScrapRepository by lazy {
        FirebaseBlogScrapRepositoryImpl(FirebaseBlogScrapRemoteDataSource())
    }

    private val sharedPreferenceReopository: SharedPreferenceReopository by lazy {
        SharedPreferenceReopositoryImpl(SharedPreferencesLocalDataSource(TripMateApp.getApp().applicationContext))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScrapDetailViewModel::class.java)) {
            return ScrapDetailViewModel(
                UpdateBlogScrapUseCase(firebaseBlogScrapRepository),
                GetUidUseCase(sharedPreferenceReopository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}