package kr.sparta.tripmate.ui.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityCommunityWriteBinding

class CommunityWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityWriteBinding.inflate(layoutInflater)

        binding.communityWriteTitle.setOnClickListener {
            val intent = Intent()
        }

    }
}
