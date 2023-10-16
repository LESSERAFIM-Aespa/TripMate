package kr.sparta.tripmate.ui.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityCommunityWriteBinding
import kr.sparta.tripmate.ui.main.MainActivity

class CommunityWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityWriteBackbutton.setOnClickListener {
            finish()
        }
        binding.communityWriteShare.setOnClickListener {
            val intent = Intent(this, CommunityDetailActivity::class.java)
            startActivity(intent)

        }
    }
}