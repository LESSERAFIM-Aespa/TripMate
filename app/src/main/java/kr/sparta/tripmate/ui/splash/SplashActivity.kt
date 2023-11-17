package kr.sparta.tripmate.ui.splash

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kr.sparta.tripmate.BuildConfig
import kr.sparta.tripmate.R
import kr.sparta.tripmate.api.Constants
import kr.sparta.tripmate.databinding.ActivitySplashBinding
import kr.sparta.tripmate.ui.login.LoginActivity
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

        updateCheck()
    }

    private fun startSplash() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = LoginActivity.newIntent(this@SplashActivity).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            startActivity(intent)
            finish()
        }, 1000)
    }

    private fun updateCheck(){
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.IMMEDIATE)) {
                val builder = AlertDialog.Builder(this@SplashActivity)
                builder.setTitle("업데이트")
                builder.setMessage("최신 버전이 있습니다. \n 업데이트를 해주세요")
                val listener = DialogInterface.OnClickListener { p0, p1 ->
                    when (p1) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            updateVersionCheck()
                            super.onBackPressed()
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                            super.onBackPressed()
                        }
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
                builder.setCancelable(false)
                builder.show()
            } else {
                startSplash()
            }
        }
    }
    private fun updateVersionCheck() {
        val packageName = "kr.sparta.tripmate"
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (e: android.content.ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }
}