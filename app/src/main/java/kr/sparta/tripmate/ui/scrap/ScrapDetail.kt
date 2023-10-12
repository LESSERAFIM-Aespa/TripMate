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
import org.json.JSONException
import org.json.JSONObject

class ScrapDetail : AppCompatActivity() {
    private val binding by lazy { ActivityScrapDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ScrapWebView()
    }

    private fun ScrapWebView() {
        binding.scrapWebview.apply {
            val data = intent.getStringExtra("scrapdata")
            var bloggerlink: String? = null
            if (data != null) {
                try {
                    val jsonData = JSONObject(data)
                    bloggerlink = jsonData.getString("bloggerlink")
                } catch (e: JSONException) {
                    Log.e("TripMates", "파싱 에러: $e")
                }
            }
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            lifecycleScope.launch {
                delay(500)
                if (!bloggerlink.isNullOrEmpty()) {
                    val url = "https://$bloggerlink"
                    loadUrl(url)
                } else {
                    Log.e("TripMates", "url 에러")
                }
            }
        }
    }
}