package kr.sparta.tripmate.ui.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import coil.load
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.databinding.ActivitySettingBinding
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.ui.login.LoginActivity
import kr.sparta.tripmate.ui.viewmodel.setting.SettingFactory
import kr.sparta.tripmate.ui.viewmodel.setting.SettingViewModel
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class SettingActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SettingActivity::class.java)
    }

    private val settingViewModel: SettingViewModel by viewModels { SettingFactory() }
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
        initViewModel()
        setBannerAds()  //하단 배너 광고

    }
    private fun initViewModel() {
        settingViewModel.settingUserData.observe(this) {
            setUpView(it)   //레이아웃에 데이터를 넣어줍니다.

        }
    }
    //레이아웃에 데이터를 넣어줍니다.
    private fun setUpView(it: UserDataEntity?) {
        binding.settingProfileImage.load(it?.profileImg)
        binding.settingLoginType.text = it?.type
        binding.settingId.text = it?.email
    }

    private fun initview() = with(binding) {
        //뷰모델 데이터를 가져옵니다.
        val uid = SharedPreferences.getUid(this@SettingActivity)
        settingViewModel.getUserData(uid)

        // 뒤로가기 버튼 입니다.
        settingToolbar.setNavigationOnClickListener {
            finish()
        }

        // 로그아웃 버튼입니다.
        settingLogout.setOnClickListener {
            auth.signOut()
            shortToast("로그아웃 되었습니다.")
            moveLogin()
        }

        // 회원탈퇴 버튼입니다.
        binding.settingWithdrawal.setOnClickListener {
            settingViewModel.removeUserData(uid)
            auth.signOut()  //로그아웃 됩니다.
            moveLogin()     //회원탈퇴 후 로그인화면으로 이동됩니다.
        }
    }

    /**
     * 로그아웃 & 회원탈퇴 후 LoginActivity로 넘어갑니다.
     */
    private fun moveLogin() {
        val intent = LoginActivity.newIntent(this@SettingActivity).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
        finish()
    }

    /**
     * 작성자 : 박성수
     * 하단 배너 광고 입니다.
     */
    private fun setBannerAds(){
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.settingAdsBanner.loadAd(adRequest)
    }
}