package kr.sparta.tripmate.ui.community

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import coil.load
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.databinding.ActivityCommunityDetailBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.ui.viewmodel.community.detail.CommunityDetailFactory
import kr.sparta.tripmate.ui.viewmodel.community.detail.CommunityDetailViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class CommunityDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ENTITY = "extra_entity"
        fun newIntentForEntity(context: Context, model: CommunityEntity): Intent =
            Intent(context, CommunityDetailActivity::class.java).apply {
                putExtra(EXTRA_ENTITY, model)
            }
    }

    private val binding: ActivityCommunityDetailBinding by lazy {
        ActivityCommunityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: CommunityDetailViewModel by viewModels {
        CommunityDetailFactory()
    }

    private val model by lazy {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_ENTITY, CommunityEntity::class.java)
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
                        CommunityEntity::class.java
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
                        communityTvDetailDescription.text = it.content
                        communityTvDetailUsername.text = it.profileNickname
                        communityImageIv.load(it.image) {
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
                        communityDetailLikecount.text = it.likes.toString()
                        communityTvDetailViewcount.text = it.views.toString()
                    }
                }

                updateView()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }


    private fun initView() = with(binding) {
        // View init
        model?.let {
            val isBoardScrap = checkIsBoardScrap(it)
            viewModel.updateIsBoardScrap(isBoardScrap)

            toggleImgUrlIsEmpty(it)
            checkIsMyPost(it)

            communityUserprofile.load(it.profileThumbnail)
            communityTvDetailTitle.text = it.title
            communityTvDetailDescription.text = it.content
            communityTvDetailUsername.text = it.profileNickname
            communityImageIv.load(it.image) {
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
            communityDetailLikecount.text = it.likes.toString()
            val currentViews = it.views?.plus(1).toString()
            communityTvDetailViewcount.text = currentViews
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
                    it1.copy(views = (it1.views ?: 0) + 1)
                )
            }
            editLauncher.launch(intent)
        }

        // 게시글 Scrap 버튼
        communityDetailLikeBtn.setOnClickListener {
            model?.let { communityEntity ->
                val uid = SharedPreferences.getUid(this@CommunityDetailActivity)

                // 스크랩 업데이트
                communityEntity.key?.let { it1 ->
                    viewModel.updateBoardScrap(uid, it1)
                }

                // 현재 스크랩상태 불러오기
                val isBoardScrap = viewModel.isBoardScrap.value

                // 현재 스크랩상태 업데이트
                isBoardScrap?.let { isScraped ->
                    viewModel.updateIsBoardScrap(!isScraped)
                }
            }
        }

        // 삭제하기
        communityDetailRemove.setOnClickListener {
            val builder = AlertDialog.Builder(this@CommunityDetailActivity)
            builder.setTitle(getString(R.string.budget_detail_dialog_title))
            builder.setMessage(getString(R.string.budget_detail_dialog_message))

            val listener = DialogInterface.OnClickListener { p0, p1 ->
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        model?.key?.let {
                            // 게시글 삭제
                            viewModel.removeBoard(it)
                            finish()
                        }
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {}
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

            builder.show()
        }
    }

    private fun initViewModel() = with(viewModel) {
        isBoardScrap.observe(this@CommunityDetailActivity) {
            toggleBoardScrap(it)
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 이미지Url유무에따라 ImageView의 Visible을 조절합니다.
     * */
    private fun toggleImgUrlIsEmpty(model: CommunityEntity) = with(binding) {
        model.image?.let { url ->
            if (url != "") {
                communityImageCardView.visibility = View.VISIBLE
            } else {
                communityImageCardView.visibility = View.GONE
            }
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 내가 쓴글인지 확인 후 수정 및 삭제버튼 생성
     * */
    private fun checkIsMyPost(model: CommunityEntity) = with(binding) {
        val uid = SharedPreferences.getUid(this@CommunityDetailActivity)
        if (model.id == uid) {
            communityDetailEdit.visibility = View.VISIBLE
            communityDetailRemove.visibility = View.VISIBLE
        } else {
            communityDetailEdit.visibility = View.GONE
            communityDetailRemove.visibility = View.GONE
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 해당 게시글이 스크랩목록에 존재하는지 여부를 알려줌
     * true- 존재함 / false- 없음
     * */
    private fun checkIsBoardScrap(model: CommunityEntity): Boolean {
        val uid = SharedPreferences.getUid(this@CommunityDetailActivity)
        val scrapUsers = model.scrapUsers.toMutableList()
        val findItem = scrapUsers.find { it == uid } ?: ""

        // 해당 게시글이 내 스크랩목록에 존재하지 않을경우
        if (findItem == "") {
            return false
        }

        return true
    }

    /**
     * 작성자: 서정한
     * 내용: 게시물 스크랩버튼의 토글기능.
     * */
    private fun toggleBoardScrap(isBoardScrap: Boolean) = with(binding) {
        if (isBoardScrap) {
            communityDetailLikeBtn.setImageResource(R.drawable.star_filled)
        } else {
            communityDetailLikeBtn.setImageResource(R.drawable.star)
        }
    }
}