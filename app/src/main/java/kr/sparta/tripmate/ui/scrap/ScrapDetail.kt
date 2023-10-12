package kr.sparta.tripmate.ui.scrap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityScrapDetailBinding

class ScrapDetail : AppCompatActivity() {
    private val binding by lazy { ActivityScrapDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val data = intent.getStringExtra("scrapdata")
        if(data != null){
            Log.d("TripMates","디테일 : ${data}")
        }
        ScrapWebView()
    }

    private fun ScrapWebView() {
        binding.scrapWebview.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            lifecycleScope.launch {
                delay(1000)
//                loadUrl()
            }
        }
    }
}