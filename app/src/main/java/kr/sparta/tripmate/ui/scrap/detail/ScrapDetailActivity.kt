package kr.sparta.tripmate.ui.scrap.detail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityScrapDetailBinding
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.ui.viewmodel.scrap.detail.ScrapDetailFactory
import kr.sparta.tripmate.ui.viewmodel.scrap.detail.ScrapDetailViewModel
import kr.sparta.tripmate.util.method.shortToast

class ScrapDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MODEL = "extra_model"
        fun newIntentForScrap(context: Context, model: SearchBlogEntity): Intent =
            Intent(context, ScrapDetailActivity::class.java).apply {
                putExtra(EXTRA_MODEL, model)
            }
    }

    private val viewModel: ScrapDetailViewModel by viewModels {
        ScrapDetailFactory()
    }

    private val binding by lazy {
        ActivityScrapDetailBinding.inflate(layoutInflater)
    }

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
        initViewModel()
    }

    private fun initView() = with(binding) {
        /**
         * 작성자: 서정한
         * 내용: 웹뷰가 로딩중일경우 프로그레스바를 보여줍니다.
         * 로딩이끝나면 프로그레스바를 화면에서 Gone합니다.
         * */
        fun isLoadingWebView(isLoading: Boolean) = with(binding) {
            if (isLoading) {
                scrapDetailProgressbar.visibility = View.VISIBLE
            } else {
                scrapDetailProgressbar.visibility = View.GONE
            }
        }

        // 해당블로그의 진입당시 isLike 설정
        model?.let {
            viewModel.updateIsScraped(it.isLike)
        }

        //WebView
        scrapDetailWebview.run {
            model?.let {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        isLoadingWebView(true)
                        // 사용자가 다른페이지로 이동을 요청할경우 원래 블로그페이지를 다시 불러옴
                        model?.let { searchBlogEntity ->
                            if (url != searchBlogEntity.link) {
                                view?.loadUrl(searchBlogEntity.link ?: "about:blank")
                                context.shortToast(getString(R.string.scrap_detail_no_move_page))
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        isLoadingWebView(false)
                    }
                }
                settings.javaScriptEnabled = true

                lifecycleScope.launch {
                    if (it.link != null) {
                        loadUrl(it.link)
                    } else {
                        Log.e("ScrapDetail", "you need check for Url : ${it.link}")
                    }
                }
            }
        }

        // 블로그 스크랩버튼
        scrapDetailLikeBtn.setOnClickListener {
            val uid = viewModel.getUid()
            // 현재 블로그 스크랩상태 업데이트
            model?.let {
                viewModel.updateBlogScrap(uid, it)
            }

            // 현재 블로그스크랩상태를 불러온 후 토글
            val nowIsScraped = viewModel.isScrap.value
            nowIsScraped?.let {
                viewModel.updateIsScraped(!it)
            }

        }

        // 뒤로가기
        scrapDetailToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initViewModel() {
        with(viewModel) {
            isScrap.observe(this@ScrapDetailActivity) {
                toggleScrapBlog(it)
            }
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 스크랩 버튼 클릭에따라 버튼Image를 업데이트합니다.
     * */
    private fun toggleScrapBlog(isScraped: Boolean) {
        if (isScraped) {
            binding.scrapDetailLikeBtn.setImageResource(R.drawable.star_filled)
        } else {
            binding.scrapDetailLikeBtn.setImageResource(R.drawable.star)
        }
    }
}