package kr.sparta.tripmate.ui.userprofile.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import kr.sparta.tripmate.databinding.ActivityUserProfileBinding
import kr.sparta.tripmate.ui.userprofile.model.UserProfileModel

/**
 * 작성자: 서정한
 * 내용: 사용자 프로필 Activity
 * */
class UserProfileActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MODEL = "extra_model"
        fun newIntentForGetUserProfile(context: Context, model: UserProfileModel): Intent =
            Intent(context, UserProfileActivity::class.java).apply {
                putExtra(EXTRA_MODEL, model)
            }
    }

    private val binding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    private val adapter by lazy{
        UserProfileTabLayoutAdapter(this@UserProfileActivity)
    }

    private val model by lazy {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_MODEL, UserProfileModel::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_MODEL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        userProfileViewpager.adapter = adapter
        // TabLayout x ViewPager2
        TabLayoutMediator(userProfileTablayout, userProfileViewpager) {tab, position ->
            tab.setText(adapter.getTitle(position))
        }.attach()

        // 뒤로가기
        userProfileToolbar.setNavigationOnClickListener {
            finish()
        }

        model?.let {
            // 썸네일
            userProfileThumbnail.load(it.thumbnail)
            // 닉네임
            userProfileNickname.text = it.nickname
            // 자기소개 한줄
            userProfileContentTextview.text = it.selfContent
        }
    }
}