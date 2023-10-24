package kr.sparta.tripmate.ui.viewmodel.home.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository
import kr.sparta.tripmate.domain.usecase.GetFirebaseBoardData
import kr.sparta.tripmate.domain.usecase.IsFirebaseBoardViews

class HomeBoardFactory : ViewModelProvider.Factory {
    private val repository: FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(
            FirebaseDBRemoteDataSource()
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeBoardViewModel::class.java)) {
            return HomeBoardViewModel(
                GetFirebaseBoardData(repository),
                IsFirebaseBoardViews(repository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}