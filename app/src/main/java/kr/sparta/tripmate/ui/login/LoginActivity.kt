package kr.sparta.tripmate.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityLoginBinding
import kr.sparta.tripmate.domain.model.UserData
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.util.method.longToast
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class LoginActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    private lateinit var login_Database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private var isLogin = false
    private val googleLogin: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    handleSignInResult(data)
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        login_Database = Firebase.database.reference

        // 앱에 로그인 되어있는지 확인하고, 로그인 되어있다면 바로 MainActivity로 이동시킵니다.
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.loginGoogle.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //기본 구글 로그인 옵션
            .requestIdToken(getString(R.string.default_web_client_id))  //서버로 전송할  Google ID토큰 (파이어베이스 프로젝트에 할당된 id)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)  //구글 로그인 클라이언트
        val signInIntent = googleSignInClient.signInIntent  //구글 로그인 클라이언트를 인텐트에 할당한다.
        googleLogin.launch(signInIntent)    //할당된 인텐트를 이용해서 구글 로그인 액티비티를 시작한다.
    }


    private fun handleSignInResult(data: Intent) {
        val task =
            GoogleSignIn.getSignedInAccountFromIntent(data)  //구글 로그인 액티비티에서 반환된 data에서 사용자 정보를 불러온다.
        try {
            val account =
                task.getResult(ApiException::class.java)  //task에서 불러온 사용자 정보를 account에 할당하고, 예외가 발생하면 바로 catch부분으로 넘어간다.(밑에줄 코드x)
            firebaseAuthWithGoogle(account.idToken!!)   //로그인에 성공했을때 구글에서 사용자를 구별할 수 있도록 보내주는 토큰이다. 위에 gso에 담긴 토큰값과 다른것이다.
        } catch (e: ApiException) {
            shortToast("Google sign in failed: ${e.message}")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(
            idToken,
            null
        )    //구글에서 보내준 토큰을 이용해 유저를 구분하고, 파이어베이스에 로그인을 시도한다.
        auth.signInWithCredential(credential)   //credential을 이용해서 firebase로 해당 유저를 로그인한다.
            .addOnCompleteListener(this) { task ->      //로그인작업이 완료될때 리스너
                if (task.isSuccessful) {
                    SuccessLogin()
                    shortToast("닉네임을 입력해주세요")

                } else {
                    FailLogin()
                    shortToast("인증에 실패 했습니다.")
                }
            }
    }

    private fun savedLogin(id: String, nickName: String, photoUrl: String, uid: String) {
        Log.d("TripMates", "아이디 : ${id}")
        Log.d("TripMates", "닉넴 : ${nickName}")
        login_Database.child("UserData").child(uid).setValue(
            UserData(
                id, nickName, photoUrl,
                uid
            )
        )
        SharedPreferences.saveUid(this,uid)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun ChangeLayout(isLogin: Boolean) {
        val nick_layout = binding.nickCenterConstraint
        val login_layout = binding.loginCenterConstraint
        if (isLogin) {
            login_layout.visibility = View.GONE
            nick_layout.visibility = View.VISIBLE

            LoginBtn()
        } else {
            login_layout.visibility = View.VISIBLE
            nick_layout.visibility = View.GONE
        }

    }

    private fun LoginBtn() {
        val user = auth.currentUser     //파이어베이스에 로그인한 사용자 정보
        binding.nickBtn.setOnClickListener {
            val input_nickName = binding.nickEdit.text.toString()
            longToast("${input_nickName}의 계정으로 로그인 되었습니다.")
            savedLogin(                                     //저장할 데이터들 함수로 보냄
                user?.email.toString(), input_nickName,
                user?.photoUrl?.toString() ?: "", user?.uid!!
            )
        }
    }

    private fun SuccessLogin() {
        isLogin = true
        ChangeLayout(isLogin)
    }

    private fun FailLogin() {
        isLogin = false
        ChangeLayout(isLogin)
    }

}