package kr.sparta.tripmate.ui.viewmodel.userproflie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveBoardFirebase

class UserProfileBoardFactory : ViewModelProvider.Factory {
    private val repository : FirebaseBoardRepository by lazy {
        FirebaseBoardRepositoryImpl(FirebaseDBRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserProfileBoardViewModel::class.java)){
            return UserProfileBoardViewModel(
                GetFirebaseBoardDataFromBoardRepo(repository),
                SaveBoardFirebase(repository)
            ) as T
        }
        throw IllegalArgumentException("에러")
    }
}