package kr.sparta.tripmate.ui.viewmodel.mypage.scrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBoardRemoteDataSource
import kr.sparta.tripmate.data.datasource.remote.community.scrap.FirebaseBlogScrapRemoteDataSource
import kr.sparta.tripmate.data.repository.community.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseBlogScrapRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBlogScrapRepository
import kr.sparta.tripmate.domain.repository.community.FirebaseBoardRepository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetAllBoardsUseCase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateBoardViewsUseCase
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetAllBlogScrapsUseCase

class MyPageScrapFactory : ViewModelProvider.Factory {
    private val blogScrapRepository: FirebaseBlogScrapRepository by lazy {
        FirebaseBlogScrapRepositoryImpl(FirebaseBlogScrapRemoteDataSource())
    }

    private val boardRepository : FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseBoardRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageScrapViewModel::class.java)) {
            return MyPageScrapViewModel(
                GetAllBlogScrapsUseCase(blogScrapRepository),
                GetAllBoardsUseCase(boardRepository),
                UpdateBoardViewsUseCase(boardRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}