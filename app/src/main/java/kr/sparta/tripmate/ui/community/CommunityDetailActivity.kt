package kr.sparta.tripmate.ui.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityCommunityDetailBinding

class CommunityDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCommunityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityDetailBackbutton.setOnClickListener {
            val intent = Intent(this,CommunityWriteActivity::class.java)
            startActivity(intent)
        }
    }
}