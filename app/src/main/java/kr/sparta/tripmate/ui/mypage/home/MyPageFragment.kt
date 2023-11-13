package kr.sparta.tripmate.ui.mypage.home

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentMyPageBinding
import kr.sparta.tripmate.domain.model.user.UserDataEntity
import kr.sparta.tripmate.ui.mypage.scrap.MyPageScrapFragment
import kr.sparta.tripmate.ui.setting.SettingActivity
import kr.sparta.tripmate.ui.viewmodel.mypage.main.MyPageFactory
import kr.sparta.tripmate.ui.viewmodel.mypage.main.MyPageViewModel

class MyPageFragment : Fragment() {
    companion object {
        fun newInstance(): MyPageFragment = MyPageFragment()
    }

    private lateinit var auth: FirebaseAuth
    private val myPageViewModel: MyPageViewModel by viewModels {
        MyPageFactory()
    }
    private lateinit var myPageContext: Context
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val adapter: MyPageTabLayoutAdapter by lazy {
        MyPageTabLayoutAdapter(requireActivity())
    }
    private val settingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myPageContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        auth = FirebaseAuth.getInstance()
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myPageViewModel.getUserData(
            myPageViewModel.getUid()
        )

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        with(myPageViewModel) {
            userData.observe(viewLifecycleOwner) {
                with(binding) {
                    mypageProfileImageview.load(it?.profileImg)
                    mypageProfileNickTextview.text = it?.nickname
                    mypageProfileContentTextview.text = it?.comment
                }
            }
        }
    }


    private fun initView() = with(binding) {

        mypageProfileContentTextview.movementMethod = ScrollingMovementMethod.getInstance()

        mypageViewpager.adapter = adapter

        TabLayoutMediator(mypageTablayout, mypageViewpager) { tab, position ->
            tab.setText(adapter.getTitle(position))
        }.attach()

        mypageViewpager.offscreenPageLimit = adapter.itemCount

        // 설정페이지 이동
        mypageSettingButton.setOnClickListener {
            val intent: Intent = SettingActivity.newIntent(requireContext())
            settingLauncher.launch(intent)
        }

        // Edit 버튼클릭시 viewType변경
        fun updateEditType(isEditMode: Boolean) {
            if (isEditMode) {
                mypageEditmodeEdittextContainer.visibility = View.VISIBLE
                mypageEditCancelButton.visibility = View.VISIBLE
                mypageEditSubmitButton.visibility = View.VISIBLE
                mypageProfileContentTextview.visibility = View.GONE
                mypageInfoTv.visibility = View.GONE
                mypageSettingButton.visibility = View.GONE
                mypageEditButton.visibility = View.GONE

            } else {
                mypageEditmodeEdittextContainer.visibility = View.GONE
                mypageEditCancelButton.visibility = View.GONE
                mypageEditSubmitButton.visibility = View.GONE
                mypageProfileContentTextview.visibility = View.VISIBLE
                mypageInfoTv.visibility = View.VISIBLE
                mypageSettingButton.visibility = View.VISIBLE
                mypageEditButton.visibility = View.VISIBLE
            }
        }

        // Edit버튼 클릭
        mypageEditButton.setOnClickListener {
            // 이전입력되었던 텍스트 가져오기(있을경우)
            val beforeText = mypageProfileContentTextview.text
            mypageProfileContentEdittext.setText(beforeText)
            updateEditType(true)
        }

        // 확인 버튼
        mypageEditSubmitButton.setOnClickListener {

            val user = auth.currentUser
            val nickName = myPageViewModel.getNickName()

            val input_EditText = binding.mypageProfileContentEdittext.text.toString()
            myPageViewModel.saveUserData(
                UserDataEntity(
                    "Google",
                    user?.email.toString(), nickName,
                    user?.photoUrl?.toString() ?: "", user?.uid!!, input_EditText
                )
            )
            updateEditType(false)
        }

        // 취소 버튼
        mypageEditCancelButton.setOnClickListener {
            editCancelDialog(
                positive = {
                    updateEditType(false)
                },
                negative = {},
            )
        }

        // EditText Watcher
        mypageProfileContentEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {
                // 글자수 textView 업데이트
                mypageProfileCheckLengthTextview.text = "${p0?.length}/30"
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    // EditText 수정중 뒤로가기 클릭시
    private fun editCancelDialog(
        positive: () -> Unit,
        negative: () -> Unit,
    ) {
        val listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> positive()

                DialogInterface.BUTTON_NEGATIVE -> negative()
            }
        }

        AlertDialog.Builder(requireContext()).apply {
            setTitle("작성취소")
            setMessage("정말 작성을 취소하시겠습니까?")
            setPositiveButton(R.string.mypage_edit_submit, listener)
            setNegativeButton(R.string.mypage_edit_cancel, listener)
        }
            .create()
            .show()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        (adapter.updateScrap() as MyPageScrapFragment).getAllScrapedData()
//        (adapter.updateBoard() as BoardFragment).updateBoard()
    }
}