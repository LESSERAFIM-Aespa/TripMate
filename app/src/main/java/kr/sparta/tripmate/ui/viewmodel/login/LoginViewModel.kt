package kr.sparta.tripmate.ui.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetNickNameDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.GetUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.SaveUserDataUseCase
import kr.sparta.tripmate.domain.usecase.firebaseuserrepository.WithdrawalUserDataUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetNickNameUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.GetUidUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.SaveNickNameUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.SaveProfileUseCase
import kr.sparta.tripmate.domain.usecase.sharedpreference.SaveUidUseCase

class LoginViewModel(
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val getNickNameDataUseCase: GetNickNameDataUseCase,
    private val saveUidUseCase: SaveUidUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val saveNickNameUseCase: SaveNickNameUseCase,
    private val getUidUseCase: GetUidUseCase,
    private val getNickNameUseCase: GetNickNameUseCase,
    private val withdrawalUserDataUseCase: WithdrawalUserDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) :
    ViewModel() {
    private val auth = FirebaseAuth.getInstance()

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

    suspend fun getNickNameData(nickName: String): Boolean = getNickNameDataUseCase(nickName)

    fun saveUid(uid: String) = saveUidUseCase(uid)
    fun saveProfile(profile: String) = saveProfileUseCase(profile)
    fun saveNickName(nickName: String) = saveNickNameUseCase(nickName)
    fun getUid(): String = getUidUseCase()
    fun getNickName(): String = getNickNameUseCase()

    fun removeUserData(uid: String) = withdrawalUserDataUseCase(uid)

    fun getUserData(uid: String) = getUserDataUseCase(uid)
}