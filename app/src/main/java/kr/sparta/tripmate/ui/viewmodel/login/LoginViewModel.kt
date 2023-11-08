package kr.sparta.tripmate.ui.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.model.user.toEntity
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetNickNameDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.SaveUserDataUseCase

class LoginViewModel(
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val getNickNameDataUseCase: GetNickNameDataUseCase

) :
    ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _userDatas = MutableLiveData<UserDataEntity?>()
    val userDatas : LiveData<UserDataEntity?> get() = _userDatas

    /**
     * 작성자: 서정한
     * 내용: 사용자가 Firebase에 등록되어있는지 확인함.
     * 없을경우 null을 return함.
     * */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    /**
     * 작성자: 서정한
     * 내용: Firebase RDB에 로그인한 유저정보를 저장함.
     * */
    fun saveCurrentUser(model: UserDataEntity) = saveUserDataUseCase(model)

    suspend fun getNickNameData(nickName: String): Boolean =  getNickNameDataUseCase(nickName)
}