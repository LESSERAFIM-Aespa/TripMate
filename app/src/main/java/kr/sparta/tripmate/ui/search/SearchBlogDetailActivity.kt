package kr.sparta.tripmate.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivitySearchBlogDetailBinding
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.ui.viewmodel.searchblog.detail.SearchBlogDetailFactory
import kr.sparta.tripmate.ui.viewmodel.searchblog.detail.SearchBlogDetailViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class SearchBlogDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MODEL = "extra_model"
        fun newIntentForScrap(context: Context, model: SearchBlogEntity): Intent =
            Intent(context, SearchBlogDetailActivity::class.java).apply {
                putExtra(EXTRA_MODEL, model)
            }
    }

    private val viewModel: SearchBlogDetailViewModel by viewModels { SearchBlogDetailFactory() }
    private val binding by lazy { ActivitySearchBlogDetailBinding.inflate(layoutInflater) }

    private val model by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_MODEL, SearchBlogEntity::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_MODEL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() = with(binding) {
        model?.let { it ->
            // init WebView
            scrapWebview.let { webview ->
                webview.webViewClient = WebViewClient()
                webview.settings.javaScriptEnabled = true

                lifecycleScope.launch {
                    delay(500)
                    if (it.url.isNotEmpty() || it.url != "") {
                        webview.loadUrl(it.url)
                    } else {
                        Log.e("ScrapDetail", "you need check for Url : ${it.url}")
                    }
                }
            }
        }

        // 좋아요버튼
        scrapDetailLikeBtn.setOnClickListener {
            val uid = SharedPreferences.getUid(this@SearchBlogDetailActivity)

            model?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    // Toggle Like Button
                    if (it.isLike) {
                        scrapDetailLikeBtn.setImageResource(R.drawable.paintedstar)
                    } else {
                        scrapDetailLikeBtn.setImageResource(R.drawable.hollowstar)
                    }

                    // 블로그 북마크여부 업데이트
                    viewModel.updateBookmarkedBlog(uid, it)
                }
            }
        }
    }
}