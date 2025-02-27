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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityCommunityDetailBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.ui.userprofile.main.UserProfileActivity
import kr.sparta.tripmate.ui.viewmodel.community.detail.CommunityDetailFactory
import kr.sparta.tripmate.ui.viewmodel.community.detail.CommunityDetailViewModel

@AndroidEntryPoint
class CommunityDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_KEY = "extra_key"
        fun newIntentForEntity(context: Context, boardKey: String): Intent =
            Intent(context, CommunityDetailActivity::class.java).apply {
                putExtra(EXTRA_KEY, boardKey)
            }
    }

    private val binding: ActivityCommunityDetailBinding by lazy {
        ActivityCommunityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: CommunityDetailViewModel by viewModels {
        CommunityDetailFactory()
    }

    private val boardKey by lazy {
        intent.getStringExtra(EXTRA_KEY)
    }

    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Write의 Model 받아오기
                val edit = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(
                        EXTRA_KEY,
                        CommunityEntity::class.java
                    )
                } else {
                    result.data?.getParcelableExtra(EXTRA_KEY)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        getAllBoards()
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

        val comparedLikeUid = comparedUid(item.likeUsers)
        val comparedScrapUid = comparedUid(item.scrapUsers)
        toggleBoardScrap(comparedScrapUid)  //북마크 아이콘 상태
        toggleIsLikeIcon(comparedLikeUid)  //좋아요 아이콘 상태

        // 게시글을 수정하는 클릭 이벤트
        btnCommunityDetailEdit(item)

        // 게시글을 북마크하는 클릭 이벤트
        btnCommunityDetailLikeBtn(item)

        // 게시글을 삭제하는 기능을 가진 클릭 이벤트
        btnCommunityDetailRemove(item)

        // 좋아요 기능 클릭 이벤트
        btnCommunityIvLike(item)

        //
        btnCommunityUserprofile(item)


        // 뒤로가기
        communityDetailToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun btnCommunityUserprofile(item: CommunityEntity) {
        binding.communityUserprofile.setOnClickListener {
            val intent = UserProfileActivity.newIntentForGetUserProfile(
                this@CommunityDetailActivity,
                item
            )
            startActivity(intent)
        }
    }

    /**
     * 작성자 : 서정한
     * 내용 : 게시글을 수정하는 클릭이벤트 입니다.
     */
    private fun btnCommunityDetailEdit(item: CommunityEntity) {
        binding.communityDetailEdit.setOnClickListener {
            val intent = item.let { it1 ->
                CommunityWriteActivity.newIntentForEdit(
                    this@CommunityDetailActivity,
                    it1
                )
            }
            editLauncher.launch(intent)
        }
    }

    /**
     * 작성자 : 서정한
     * 내용 : 게시글을 북마크 기능을 하는 클릭 이벤트 입니다.
     */
    private fun btnCommunityDetailLikeBtn(item: CommunityEntity) {
        binding.communityDetailLikeBtn.setOnClickListener {
            item.let { communityEntity ->
                val uid = viewModel.getUid()

                // 스크랩 업데이트
                communityEntity.key?.let { it1 ->
                    viewModel.updateBoardScrap(uid, it1)
                }
            }
        }
    }

    /**
     * 작성자 : 서정한
     * 내용 : 게시글을 삭제하는 클릭 이벤트 입니다.
     */
    private fun btnCommunityDetailRemove(item: CommunityEntity) {
        binding.communityDetailRemove.setOnClickListener {
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
    }

    /**
     * 작성자 : 박성수
     * 내용 : 좋아요 기능 클릭 이벤트 입니다.
     */
    private fun btnCommunityIvLike(item: CommunityEntity) {
        binding.communityIvLike.setOnClickListener {
            item.let {
                val uid = viewModel.getUid()

                viewModel.updateBoardLike(
                    uid,
                    item
                )
            }
        }
    }

    private fun comparedUid(uidList: List<String>): Boolean =
        uidList.any { it == viewModel.getUid() }


    private fun initViewModel() = with(viewModel) {
        /**
         * 작성자 : 박성수
         * 내용 : RDB데이터를 관찰하고, 업데이트 합니다.
         */
        boards.observe(this@CommunityDetailActivity) { CommunityEntity ->
            CommunityEntity?.let {
                initView(it)
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
        val uid = viewModel.getUid()
        if (model.userid == uid) {
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

    /**
     * 작성자 : 박성수
     * 내용 : 좋아요 버튼 토글 기능입니다.
     */
    private fun toggleIsLikeIcon(compareUid: Boolean) = with(binding) {
        if (compareUid) {
            communityIvLike.setImageResource(R.drawable.paintedheart)
        } else {
            communityIvLike.setImageResource(R.drawable.heart)
        }
    }

    /**
     * 작성자 : 박성수
     * 내용 : RDB데이터를 가져옵니다.
     */
    private fun getAllBoards() = CoroutineScope(Dispatchers.Main).launch {
        boardKey?.let {
            viewModel.getBoard(it)
        }
    }
}