package kr.sparta.tripmate.ui.userprofile.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kr.sparta.tripmate.databinding.ActivityUserProfileBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.ui.viewmodel.userproflie.UserProfileFactory
import kr.sparta.tripmate.ui.viewmodel.userproflie.UserProfileViewModel

/**
 * 작성자: 서정한
 * 내용: 사용자 프로필 Activity
 * */
@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MODEL = "extra_model"
        fun newIntentForGetUserProfile(context: Context, model: CommunityEntity): Intent =
            Intent(context, UserProfileActivity::class.java).apply {
                putExtra(EXTRA_MODEL, model)
            }
    }

    private val binding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        UserProfileTabLayoutAdapter(this@UserProfileActivity)
    }
    private val userProfileViewModel: UserProfileViewModel by viewModels { UserProfileFactory() }

    private val model by lazy {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_MODEL, CommunityEntity::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_MODEL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        model?.userid?.let { userProfileViewModel.saveUidFromUser(it) }
        initView()
        initViewModel()
        callDataSource()
    }

    private fun callDataSource() {
        model?.let {
            model?.userid?.let { it1 -> userProfileViewModel.getUserData(it1) }
        }
    }

    private fun initViewModel() {
        with(userProfileViewModel) {
            userProfileResult.observe(this@UserProfileActivity) {
                with(binding) {
                    userProfileThumbnail.load(it?.profileImg)
                    userProfileNickTextview.text = it?.nickname
                    userProfileContentTextview.text = it?.comment
                }
            }
        }
    }

    private fun initView() = with(binding) {
        userProfileViewpager.adapter = adapter
        // TabLayout x ViewPager2
        TabLayoutMediator(userProfileTablayout, userProfileViewpager) { tab, position ->
            tab.setText(adapter.getTitle(position))
        }.attach()

        // 뒤로가기
        userToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}