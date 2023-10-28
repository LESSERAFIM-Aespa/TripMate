package kr.sparta.tripmate.ui.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.data.datasource.remote.FirebaseDBRemoteDataSource
import kr.sparta.tripmate.data.repository.FirebaseBoardRepositoryImpl
import kr.sparta.tripmate.domain.repository.FirebaseBoardRepository
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveBoardFirebase
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.UpdateCommuIsViewFromBoardRepo

class BoardFactory : ViewModelProvider.Factory {
    private val repository : FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseDBRemoteDataSource())
    }
    override fun <T: ViewModel> create(modelClass: Class<T>) :T{
        if(modelClass.isAssignableFrom(BoardViewModel::class.java)){
            return BoardViewModel(
                GetFirebaseBoardDataFromBoardRepo(repository),
                SaveBoardFirebase(repository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}