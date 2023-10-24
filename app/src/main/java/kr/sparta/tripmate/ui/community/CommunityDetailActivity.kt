package kr.sparta.tripmate.ui.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import coil.load
import kr.sparta.tripmate.databinding.ActivityCommunityDetailBinding
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity

class CommunityDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityDetailBinding
    private lateinit var writeItem: CommunityModelEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //데이터를 받는 부분
        writeItem = intent.getParcelableExtra<CommunityModelEntity>("Data")!!

        Log.d("CommunityDetailfxxk",writeItem.addedImage.toString())
        if (!writeItem.addedImage.isNullOrBlank()) {
            binding.communityIvAddImage.visibility = View.VISIBLE
            setupViews()
        } else {
            binding.communityIvAddImage.visibility = View.GONE
            setupViews()
        }

        binding.communityDetailBackbutton.setOnClickListener {
            finish()
        }
    }
    private fun setupViews() {
        binding.communityUserprofile.load(writeItem.profileThumbnail)
        binding.communityTvDetailTitle.text = writeItem.title
        binding.communityTvDetailDescription.text = writeItem.body
        binding.communityTvDetailUsername.text = writeItem.profileNickname
        binding.communityIvAddImage.load(writeItem.addedImage)
        binding.communityDetailLikecount.text = writeItem.likes
        binding.communityTvDetailViewcount.text = writeItem.views
    }
}