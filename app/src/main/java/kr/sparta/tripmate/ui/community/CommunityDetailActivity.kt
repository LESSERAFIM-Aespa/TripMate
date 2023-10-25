package kr.sparta.tripmate.ui.community

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import coil.load
import kr.sparta.tripmate.databinding.ActivityCommunityDetailBinding
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity

class CommunityDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ENTITY = "extra_entity"
        fun newIntentForEntity(context: Context, model: CommunityModelEntity): Intent =
            Intent(context, CommunityDetailActivity::class.java).apply {
                putExtra(EXTRA_ENTITY, model)
            }
    }

    private val binding: ActivityCommunityDetailBinding by lazy {
        ActivityCommunityDetailBinding.inflate(layoutInflater)
    }

    private val model by lazy {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_ENTITY, CommunityModelEntity::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_ENTITY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        /**
         * 작성자: 서정한
         * 내용: 이미지Url유무에따라 ImageView의 Visible을 조절합니다.
         * */
        fun toggleImgUrlIsEmpty(model: CommunityModelEntity) {
            model?.addedImage?.let { url ->
                if (url.isNotEmpty()) {
                    communityImageCardView.visibility = View.VISIBLE
                } else {
                    communityImageCardView.visibility = View.GONE
                }
            }
        }

        // View init
        model?.let {
            toggleImgUrlIsEmpty(it)
            communityUserprofile.load(it.profileThumbnail)
            communityTvDetailTitle.text = it.title
            communityTvDetailDescription.text = it.description
            communityTvDetailUsername.text = it.profileNickname
            communityImageIv.load(it.addedImage)
            communityDetailLikecount.text = it.likes
            communityTvDetailViewcount.text = it.views
        }

        // 뒤로가기
        communityDetailToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}