package kr.sparta.tripmate.ui.community

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import kr.sparta.tripmate.databinding.ActivityCommunityDetailBinding
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

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

    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Write의 Model 받아오기
                val edit = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(
                        EXTRA_ENTITY,
                        CommunityModelEntity::class.java
                    )
                } else {
                    result.data?.getParcelableExtra(EXTRA_ENTITY)
                }

                /**
                 * 작성자: 서정한
                 * 내용: 사용자가 자기가 작성한 게시판 글을 수정했을경우
                 * WritePage에서 수정된 글 Model을 받아와 View에 적용해준다.
                 * */
                fun updateView() = with(binding) {
                    edit?.let {
                        toggleImgUrlIsEmpty(it)
                        checkIsMyPost(it)
                        communityUserprofile.load(it.profileThumbnail)
                        communityTvDetailTitle.text = it.title
                        communityTvDetailDescription.text = it.description
                        communityTvDetailUsername.text = it.profileNickname
                        communityImageIv.load(it.addedImage){
                            crossfade(true)
                            listener(
                                onStart = {
                                    // 로딩시작
                                    communityDetailImageProgressbar.visibility = View.VISIBLE
                                },
                                onSuccess = { request, result ->
                                    // 로딩종료
                                    communityDetailImageProgressbar.visibility = View.GONE
                                }
                            )
                        }
                        communityDetailLikecount.text = it.likes
                        communityTvDetailViewcount.text = it.views
                    }
                }

                updateView()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        // View init
        model?.let {
            toggleImgUrlIsEmpty(it)
            checkIsMyPost(it)
            communityUserprofile.load(it.profileThumbnail)
            communityTvDetailTitle.text = it.title
            communityTvDetailDescription.text = it.description
            communityTvDetailUsername.text = it.profileNickname
            communityImageIv.load(it.addedImage) {
                crossfade(true)
                listener(
                    onStart = {
                        // 로딩시작
                        communityDetailImageProgressbar.visibility = View.VISIBLE
                    },
                    onSuccess = { request, result ->
                        // 로딩종료
                        communityDetailImageProgressbar.visibility = View.GONE
                    }
                )
            }
            val currentViews = it.views?.toIntOrNull() ?: 0
            val newViews = currentViews+1
            communityDetailLikecount.text = it.likes
            communityTvDetailViewcount.text = newViews.toString()
        }

        // 뒤로가기
        communityDetailToolbar.setNavigationOnClickListener {
            finish()
        }

        // 수정하기
        communityDetailEdit.setOnClickListener {
            val intent = model?.let { it1 ->
                CommunityWriteActivity.newIntentForEdit(
                    this@CommunityDetailActivity,
                    it1
                )
            }
            editLauncher.launch(intent)
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 이미지Url유무에따라 ImageView의 Visible을 조절합니다.
     * */
    private fun toggleImgUrlIsEmpty(model: CommunityModelEntity) = with(binding) {
        model?.addedImage?.let { url ->
            if (url != "") {
                communityImageCardView.visibility = View.VISIBLE
            } else {
                communityImageCardView.visibility = View.GONE
            }
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 내가 쓴글인지 확인 후 수정버튼 생성
     * */
    private fun checkIsMyPost(model: CommunityModelEntity) = with(binding) {
        val uid = SharedPreferences.getUid(this@CommunityDetailActivity)
        if (model.id == uid) {
            communityDetailEdit.visibility = View.VISIBLE
        } else {
            communityDetailEdit.visibility = View.GONE
        }
    }

}