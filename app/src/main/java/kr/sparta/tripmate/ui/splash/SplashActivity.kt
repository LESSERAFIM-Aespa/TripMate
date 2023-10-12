package kr.sparta.tripmate.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kr.sparta.tripmate.databinding.ActivitySplashBinding
import kr.sparta.tripmate.ui.main.MainActivity

/**
 * 작성자: 서정한
 * 내용: 스플레시 화면구성.
 * */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startSplash()
    }

    private fun startSplash() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = MainActivity.newIntent(this@SplashActivity).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            startActivity(intent)
            finish()
        }, 1000)
    }
}