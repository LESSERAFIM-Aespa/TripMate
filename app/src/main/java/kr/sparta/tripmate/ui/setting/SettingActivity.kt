package kr.sparta.tripmate.ui.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.databinding.ActivitySettingBinding
import kr.sparta.tripmate.ui.login.LoginActivity
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class SettingActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SettingActivity::class.java)
    }

    private lateinit var setting_Database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val binding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setting_Database = Firebase.database.reference
        setContentView(binding.root)

        initview()
    }

    private fun initview() = with(binding) {
        // 뒤로가기
        settingToolbar.setNavigationOnClickListener {
            finish()
        }

        // 로그아웃
        settingLogout.setOnClickListener {
            auth.signOut()
            shortToast("로그아웃 되었습니다.")
            moveLogin()
        }

        //회원탈퇴
        settingWithdrawal.setOnClickListener {
            deleteSettingFirebase()
            auth.signOut()
            moveLogin()
        }
    }

    private fun deleteSettingFirebase() {
        val uid = SharedPreferences.getUid(this)
        val settingRef = SettingRef()

        settingRef.child(uid).removeValue().addOnSuccessListener {
            shortToast("정상적으로 탈퇴 되었습니다.")
        }
            .addOnFailureListener {
                Log.d("TripMates", "에러 : $it")
            }
    }

    private fun SettingRef(): DatabaseReference {
        return setting_Database.child("UserData")
    }

    private fun moveLogin() {
        val intent = LoginActivity.newIntent(this@SettingActivity).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
        finish()
    }
}