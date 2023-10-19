package kr.sparta.tripmate.ui.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityCommunityDetailBinding
import kr.sparta.tripmate.ui.community.main.CommunityModel

class CommunityDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityDetailBinding
    private lateinit var writeItem: CommunityModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //데이터를 받는 부분
        writeItem = intent.getParcelableExtra<CommunityModel>("Data")!!
        setupViews()

        binding.communityDetailBackbutton.setOnClickListener {
            finish()
        }
    }
    private fun setupViews() {
        binding.communityUserprofile.load(writeItem.profileThumbnail)
        binding.communityDetailTitle.text = writeItem.title
        binding.communityDetailDescription.text = writeItem.body
        binding.communityDetailUsername.text = writeItem.profileNickname
    }
}
