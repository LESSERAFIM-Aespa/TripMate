package kr.sparta.tripmate.ui.community

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import coil.load
import kr.sparta.tripmate.databinding.ActivityCommunityWriteBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.ui.viewmodel.community.write.CommunityWriteFactory
import kr.sparta.tripmate.ui.viewmodel.community.write.CommunityWriteViewModel
import kr.sparta.tripmate.util.method.isWindowTouchable
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences
import java.util.regex.Pattern


/**
 * 작성자 : 박성수
 * 1~14까지 확인하시고 이상한거 있으면 말씀해주세요
 * 기존의 코드는 주석처리해놨습니다. 확인하시고 말씀해주시면 삭제하겠습니다.
 */
class CommunityWriteActivity : AppCompatActivity() {
    companion object {
        const val MODEL_EDIT = "model_edit"

        fun newIntentForWrite(context: Context): Intent =
            Intent(context, CommunityWriteActivity::class.java)

        fun newIntentForEdit(context: Context, model: CommunityEntity): Intent =
            Intent(context, CommunityWriteActivity::class.java).apply {
                putExtra(MODEL_EDIT, model)
            }
    }

    private val binding: ActivityCommunityWriteBinding by lazy {
        ActivityCommunityWriteBinding.inflate(layoutInflater)
    }

    private val viewModel: CommunityWriteViewModel by viewModels() {
        CommunityWriteFactory()
    }

    private val model by lazy {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MODEL_EDIT, CommunityEntity::class.java)
        } else {
            intent.getParcelableExtra(MODEL_EDIT)
        }
    }

    // 겔러리 이미지가져오기
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                binding.communityWriteImageIcon.visibility = View.GONE
                val imageUri = result.data?.data
                imageUri?.let { binding.communityWriteImage.setImageURI(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        // Edit진입. 이전게시판값 불러오기
        model?.let {
            communityWriteTitle.setText(it.title)
            communityWriteDescription.setText(it.content)
            if (it.image == "") {
                communityWriteImageIcon.visibility = View.VISIBLE
            } else {
                communityWriteImageIcon.visibility = View.INVISIBLE
                communityWriteImage.load(it.image)
            }
        }

        // 뒤로가기
        communityWriteToolbar.setNavigationOnClickListener {
            finish()
        }

        /**
         * 작성자: 서정한
         * 내용: 타이틀 입력값 검증
         * */
        communityWriteTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val titleRegex = Pattern.compile("^[ㄱ-ㅣ가-힣a-zA-Z0-9\\s]*$")
                val matcher = s?.let { it1 -> titleRegex.matcher(it1) }
                matcher?.let {
                    if (!it.find()) {
                        Toast.makeText(
                            this@CommunityWriteActivity,
                            "제목은 영어, 한글, 숫자만 입력가능합니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        })

        // 게시
        communityWritePost.setOnClickListener {
            /**
             * 작성자: 서정한
             * 내용: 새로운 글을 작성하여 Firebase RDB로 업로드합니다.
             * RDB의 경우 기존자료를 업데이트할때에도 새로운 리스트를 만들어 덮어씌우는 형태입니다.
             * 그래서 수정하는경우에도 새롭게작성하는것과 로직상 차이가 없습니다.
             * */
            fun editItem(
                key: String,
                scrapUsers: List<String>,
                likeUsers: List<String>
            ): CommunityEntity =
                CommunityEntity(
                    userid = SharedPreferences.getUid(this@CommunityWriteActivity),
                    key = model?.key ?: key,
                    title = binding.communityWriteTitle.text.toString(),
                    content = binding.communityWriteDescription.text.toString(),
                    profileNickname = SharedPreferences.getNickName(this@CommunityWriteActivity),
                    profileThumbnail = SharedPreferences.getProfile(this@CommunityWriteActivity),
                    views = model?.views ?: 0,
                    likes = model?.likes ?: 0,
                    image = "",
                    likeUsers = likeUsers,
                    scrapUsers = scrapUsers,
                )

            fun postWrite() {
                val bitmap: Bitmap? = if (communityWriteImage.drawable != null) {
                    (communityWriteImage.drawable as BitmapDrawable).bitmap
                } else {
                    null
                }
                if (model?.key.isNullOrEmpty()) {
                    val key = viewModel.getCommunityKey()
                    val imgName = key.substring(key.length - 17, key.length)
                    val newModel = editItem(key, listOf(), listOf())
                    // 새 글 작성 or Edit
                    viewModel.addCommunityWrite(
                        imgName = imgName,
                        image = bitmap,
                        item = newModel,
                        context = this@CommunityWriteActivity,
                    )
                    // 로딩시작
                    viewModel.setAddLoadingState(true)
                } else {
                    val key = model?.key
                    val scrapUsers = model?.scrapUsers ?: listOf()
                    val likeUsers = model?.likeUsers ?: listOf()
                    val imgName = key?.substring(key.length - 17, key.length)
                    imgName?.let {
                        val newModel = editItem(key, scrapUsers, likeUsers)
                        viewModel.updateCommunityWrite(
                            imgName,
                            bitmap,
                            newModel,
                            this@CommunityWriteActivity
                        )
                    }
                    viewModel.setEditLoadingState(true)
                }
            }
            val titleRegex = "^[ㄱ-ㅣ가-힣a-zA-Z0-9\\s]*".toRegex()
            val communityWriteTitle = binding.communityWriteTitle.text.toString().trim()
            val communityWriteDescription= binding.communityWriteDescription.text.toString().trim()
            if (communityWriteTitle.isNullOrBlank() || communityWriteDescription.isNullOrBlank()
            ) {
                shortToast("제목과 내용을 입력해주셔야 합니다.")
                return@setOnClickListener
            } else if(!communityWriteTitle.matches(titleRegex)){
                shortToast("제목은 영어, 한글, 숫자만 입력 가능합니다.")
                return@setOnClickListener
            }
            // 새로운 글 생성
            postWrite()
        }

        // 기기이미지 불러오기
        communityWriteCardView.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            imageLauncher.launch(gallery)
        }
    }

    private fun initViewModel() {
        with(viewModel) {
            // 로딩처리
            isAddLoading.observe(this@CommunityWriteActivity) { isAddLoading ->
                communityDataIsLoading(isAddLoading, "글이 생성 되었습니다.")
            }
            isEditLoading.observe(this@CommunityWriteActivity) { isEditLoading ->
                communityDataIsLoading(isEditLoading, "게시글이 수정 되었습니다.")
            }
        }
    }

    private fun communityDataIsLoading(isLoading: Boolean, toastMessage: String) {
        if (isLoading) {
            binding.communityWriteProgressBar.visibility = View.VISIBLE
            isWindowTouchable(this@CommunityWriteActivity, true)
        } else {
            binding.communityWriteProgressBar.visibility = View.GONE
            shortToast(toastMessage)
        }
    }

}