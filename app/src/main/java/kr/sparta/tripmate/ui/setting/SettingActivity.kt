package kr.sparta.tripmate.ui.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) : Intent = Intent(context, SettingActivity::class.java)
    }

    private val binding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initview()
    }

    private fun initview()=with(binding) {
        // 뒤로가기
        settingToolbar.setNavigationOnClickListener {
            finish()
        }

        // 로그아웃
        settingLogout.setOnClickListener {

        }

        //회원탈퇴
        settingWithdrawal.setOnClickListener {

        }
    }
}