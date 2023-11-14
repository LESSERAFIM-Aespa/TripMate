package kr.sparta.tripmate.ui.viewmodel.scrap.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.api.naver.NaverNetWorkClient
import kr.sparta.tripmate.data.datasource.local.sharedpreference.SharedPreferencesLocalDataSource
import kr.sparta.tripmate.data.datasource.remote.community.scrap.FirebaseBlogScrapRemoteDataSource
import kr.sparta.tripmate.data.repository.ImageRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseBlogScrapRepositoryImpl
import kr.sparta.tripmate.data.repository.search.SearchRepositoryImpl
import kr.sparta.tripmate.data.repository.sharedpreference.SharedPreferenceReopositoryImpl
import kr.sparta.tripmate.domain.repository.ImageRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseBlogScrapRepository
import kr.sparta.tripmate.domain.repository.search.SearchRepository
import kr.sparta.tripmate.domain.repository.sharedpreference.SharedPreferenceReopository
import kr.sparta.tripmate.domain.usecase.GetImageUseCase
import kr.sparta.tripmate.domain.usecase.GetSearchBlogUseCase
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetAllBlogScrapsUseCase
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.UpdateBlogScrapUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import kr.sparta.tripmate.util.TripMateApp

class SearchBlogFactory : ViewModelProvider.Factory {
    private val searchRepository: SearchRepository = SearchRepositoryImpl(
        NaverNetWorkClient.apiService
    )
    private val blogScrapRepository: FirebaseBlogScrapRepository = FirebaseBlogScrapRepositoryImpl(
        FirebaseBlogScrapRemoteDataSource()
    )
    private val imageRepository: ImageRepository = ImageRepositoryImpl(
        NaverNetWorkClient.imageApiService
    )

    private val sharedPreferenceReopository: SharedPreferenceReopository by lazy {
        SharedPreferenceReopositoryImpl(SharedPreferencesLocalDataSource(TripMateApp.getApp().applicationContext))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBlogViewModel::class.java)) {
            return SearchBlogViewModel(
                GetSearchBlogUseCase(searchRepository),
                UpdateBlogScrapUseCase(blogScrapRepository),
                GetAllBlogScrapsUseCase(blogScrapRepository),
                GetImageUseCase(imageRepository),
                GetUidUseCase(sharedPreferenceReopository)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}