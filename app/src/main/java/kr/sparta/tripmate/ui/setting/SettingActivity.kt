package kr.sparta.tripmate.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initview()
    }

    private fun initview()=with(binding) {
        // 로그아웃
        settingLogout.setOnClickListener {

        }

        //회원탈퇴
        settingWithdrawal.setOnClickListener {
            
        }
    }
}