package kr.sparta.tripmate.ui.userprofile.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kr.sparta.tripmate.databinding.ActivityUserProfileBinding
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.ui.userprofile.model.UserProfileModel
import kr.sparta.tripmate.ui.viewmodel.userproflie.UserProfileFactory
import kr.sparta.tripmate.ui.viewmodel.userproflie.UserProfileViewModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

/**
 * 작성자: 서정한
 * 내용: 사용자 프로필 Activity
 * */
class UserProfileActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MODEL = "extra_model"
        fun newIntentForGetUserProfile(context: Context, model: CommunityModelEntity): Intent =
            Intent(context, UserProfileActivity::class.java).apply {
                putExtra(EXTRA_MODEL, model)
            }
    }

    private lateinit var auth: FirebaseAuth
    private val binding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        UserProfileTabLayoutAdapter(this@UserProfileActivity)
    }
    private val userProfileViewModel: UserProfileViewModel by viewModels { UserProfileFactory() }

    private val model by lazy {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_MODEL, CommunityModelEntity::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_MODEL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SharedPreferences.saveUidFromUser(this, model!!.id)
        initView()
        initViewModel()
        callDataSource()
    }

    private fun callDataSource() {
        model?.let {
            userProfileViewModel.updateUserData(model!!.id)
        }
    }

    private fun initViewModel() {
        with(userProfileViewModel) {
            userProfileResult.observe(this@UserProfileActivity) {
                with(binding) {
                    userProfileThumbnail.load(it?.login_profile)
                    userProfileNickname.text = it?.login_NickName
                    userProfileContentTextview.text = it?.login_coment
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
        userProfileToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}