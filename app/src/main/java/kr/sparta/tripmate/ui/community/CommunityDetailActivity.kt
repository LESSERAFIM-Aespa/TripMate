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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        updateUI()
    }


    private fun initView(item: CommunityEntity) = with(binding) {
        // View init
        item.let {

            toggleImgUrlIsEmpty(it)     //이미지가 없을떄 이미지뷰 숨기기
            checkIsMyPost(it)           //내가 쓴 글인지 확인

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
        val comparedScrapUid =
            item.scrapUsers.any { it == SharedPreferences.getUid(this@CommunityDetailActivity) }
        val comparedLikeUid =
            item.likeUsers.any { it == SharedPreferences.getUid(this@CommunityDetailActivity) }
        toggleBoardScrap(comparedScrapUid)  //북마크 아이콘 상태
        toggleIsLikeIcon(comparedLikeUid)  //좋아요 아이콘 상태
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
            item.let { communityEntity ->
                val uid = SharedPreferences.getUid(this@CommunityDetailActivity)

                // 스크랩 업데이트
                communityEntity.key?.let { it1 ->
                    viewModel.updateBoardScrap(uid, it1)
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
                        item.key?.let {
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

        //좋아요 기능
        communityIvLike.setOnClickListener {
            item.let {
                val uid = SharedPreferences.getUid(this@CommunityDetailActivity)

                viewModel.updateBoardLike(
                    uid,
                    item
                )
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        /**
         * 작성자 : 박성수
         * 내용 : RDB데이터를 관찰하고, 업데이트 합니다.
         */
        boards.observe(this@CommunityDetailActivity) { boardItems ->
            val item = boardItems.find { it?.key == model?.key }
            item?.let {
                initView(item)
            }
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
     * 내용: 게시물 스크랩버튼의 토글기능.
     * */
    private fun toggleBoardScrap(compareUid: Boolean) = with(binding) {
        if (compareUid) {
            communityDetailLikeBtn.setImageResource(R.drawable.star_filled)
        } else {
            communityDetailLikeBtn.setImageResource(R.drawable.star)
        }
    }

    private fun toggleIsLikeIcon(compareUid: Boolean) = with(binding) {
        if (compareUid) {
            binding.communityIvLike.setImageResource(R.drawable.paintedheart)
        } else {
            binding.communityIvLike.setImageResource(R.drawable.heart)
        }
    }

    private fun updateUI() = CoroutineScope(Dispatchers.Main).launch {
        viewModel.getAllBoards()
    }
}