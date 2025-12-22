package kr.sparta.tripmate.ui.login

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityLoginBinding
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.ui.viewmodel.login.LoginViewModel
import kr.sparta.tripmate.util.method.longToast
import kr.sparta.tripmate.util.method.setMaxLength
import kr.sparta.tripmate.util.method.shortToast

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    private lateinit var login_Database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()

    private val googleLogin: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            /**
             * 작성자: 박성수
             * 내용: 로그인 성공시 혹은 실패시 보여줄 레이아웃의 Visible을 로그인 성공여부에따라 반응함
             * */
            fun layoutVisibleController(isLoginSuccessful: Boolean) = with(binding) {
                if (isLoginSuccessful) {
                    if (!viewModel.getUid().isNullOrBlank()) {
                        loginCenterConstraint.visibility = View.VISIBLE
                        nickCenterConstraint.visibility = View.GONE
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        //sharedpreference에 저장된 uid가 없을때( 신규가입이거나,앱을 삭제하거나, 회원탈퇴를하거나, 구글로그인을했는데 닉네임을 입력안했을떄)
                        CoroutineScope(Dispatchers.Main).launch{
                            viewModel.getCurrentUser()?.uid?.let { currentUser ->
                                viewModel.getUserData(currentUser).collect(){user ->
                                    if(user == null){
                                        loginCenterConstraint.visibility = View.GONE
                                        nickCenterConstraint.visibility = View.VISIBLE
                                        shortToast("닉네임을 입력해주세요")
                                    } else {
                                        with(viewModel) {
                                            user.login_Uid?.let {
                                                saveUid(it)
                                            }
                                            // profile Image
                                            user.login_profile?.let {
                                                saveProfile(it)
                                            }
                                            user.login_NickName?.let{
                                                saveNickName(it)
                                            }
                                        }
                                        loginCenterConstraint.visibility = View.GONE
                                        nickCenterConstraint.visibility = View.GONE
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                            loginCenterConstraint.visibility = View.GONE
                            nickCenterConstraint.visibility = View.VISIBLE
                            shortToast("닉네임을 입력해주세요")
                        }
                    }
                } else {
                    loginCenterConstraint.visibility = View.VISIBLE
                    nickCenterConstraint.visibility = View.GONE
                    shortToast("인증에 실패 했습니다.")
                }
            }

            /**
             * 작성자: 박성수
             * 내용: 구글에서 발급한 토큰으로 Firebase Auth 인증 시작.
             * */
            fun firebaseAuthWithGoogle(idToken: String) {
                /**
                 * 작성자: 서정한
                 * 내용: 사용자가 닉네임입력후 사용자 정보를 저장함. 이후 MainActivity로 이동.
                 * */
                fun confirmNickname() {
                    binding.nickBtn.setOnClickListener {
                        CoroutineScope(Dispatchers.Main).launch {
                            val nickName = binding.nickEdit.text.toString()
                            val isExist = viewModel.getNickNameData(nickName)
                            if (!isExist) {
                                shortToast("닉네임이 존재합니다.")
                                return@launch
                            }
                            if (nickName.trim().isEmpty()) {
                                shortToast(getString(R.string.login_exception_empty_nickname))
                                return@launch
                            }
                            if (nickName.length >= 10) {
                                shortToast(getString(R.string.please_enter_a_nickname_is_less_than_10_characters))
                                return@launch
                            }

                            viewModel.getCurrentUser()?.let { user ->
                                // 서버 업로드
                                viewModel.saveCurrentUser(
                                    model = UserDataEntity(
                                        type = "Google",
                                        email = user.email.toString(),
                                        nickname = nickName,
                                        profileImg = user.photoUrl?.toString(),
                                        uid = user.uid,
                                        comment = "",
                                    )
                                )

                                // 기기 저장
                                with(viewModel) {
                                    // uid
                                    saveUid(
                                        user.uid
                                    )
                                    // profile Image
                                    user.photoUrl?.toString()?.let { url ->
                                        saveProfile(url)
                                    }
                                    // nickname
                                    saveNickName(
                                        nickName
                                    )
                                }
                            }
                            startActivity(MainActivity.newIntent(this@LoginActivity))
                            finish()

                        }
                    }
                }

                //구글에서 보내준 토큰을 이용해 유저를 구분하고, 파이어베이스에 로그인을 시도한다.
                val credential = GoogleAuthProvider.getCredential(
                    idToken,
                    null
                )

                //credential을 이용해서 firebase로 해당 유저를 로그인한다.
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        // 로그인 성공여부에따른 화면분기점
                        layoutVisibleController(task.isSuccessful)
                        //
                        confirmNickname()
                    }
            }

            /**
             * 작성자: 박성수
             * 내용: 로그인요청결과 인증작업
             * */
            fun handleSignInResult(data: Intent) {
                kotlin.runCatching {
                    //구글 로그인 액티비티에서 반환된 data에서 사용자 정보를 불러온다.
                    GoogleSignIn.getSignedInAccountFromIntent(data)

                }
                    .onSuccess { result ->
                        val account = result.getResult(ApiException::class.java)
                        account.idToken?.let { it ->
                            //로그인에 성공했을때 구글에서 사용자를 구별할 수 있도록 보내주는 토큰이다. 위에 gso에 담긴 토큰값과 다른것이다.
                            firebaseAuthWithGoogle(it)
                        }
                    }
                    .onFailure {
                        shortToast("Google sign in failed: ${it.message}")
                    }
            }

            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    handleSignInResult(it)
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.nickEdit.setMaxLength(10)
        auth = FirebaseAuth.getInstance()
        login_Database = Firebase.database.reference

        initLogin()
    }

    private fun initLogin() {
        /**
         * 작성쟈: 서정한
         * 내용: 구글로그인 요청
         * */
        fun googleSignIn() {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = googleSignInClient.signInIntent
            googleLogin.launch(signInIntent)
        }

//        if (viewModel.getCurrentUser() != null) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        binding.loginGoogle.setOnClickListener {
            googleSignIn()
        }
        binding.loginGoogle.apply {
            setColorScheme(SignInButton.COLOR_DARK)
            setSize(SignInButton.SIZE_WIDE)
        }
    }
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@LoginActivity)
        builder.setTitle("앱 종료")
        builder.setMessage("앱을 종료하시겠습니까?")
        val listener = DialogInterface.OnClickListener { p0, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.removeUserData("")
                    super.onBackPressed()
                }

                DialogInterface.BUTTON_NEGATIVE -> {}
            }
        }

        builder.setPositiveButton(
            getString(R.string.budget_detail_dialog_positive_text),
            listener
        )
        builder.setNegativeButton(
            getString(R.string.budget_detail_dialog_negative_text),
            listener
        )

        builder.show()
    }

}