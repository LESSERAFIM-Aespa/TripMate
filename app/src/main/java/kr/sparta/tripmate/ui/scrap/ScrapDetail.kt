package kr.sparta.tripmate.ui.scrap

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.sparta.tripmate.databinding.ActivityScrapDetailBinding
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity

class ScrapDetail : AppCompatActivity() {
    companion object {
        const val EXTRA_MODEL = "extra_model"
        fun newIntentForScrap(context: Context, model: ScrapEntity): Intent =
            Intent(context, ScrapDetail::class.java).apply {
                putExtra(EXTRA_MODEL, model)
            }
    }

    private val binding by lazy { ActivityScrapDetailBinding.inflate(layoutInflater) }

    private val model by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_MODEL, ScrapEntity::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_MODEL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        scrapWebView()
    }

    private fun scrapWebView() {
        binding.scrapWebview.apply {
            model?.let {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true

                lifecycleScope.launch {
                    delay(500)
                    if (it.url.isNotEmpty()) {
                        loadUrl(it.url)
                    } else {
                        Log.e("ScrapDetail", "you need check for Url : ${it.url}")
                    }
                }
            }
        }
    }
}