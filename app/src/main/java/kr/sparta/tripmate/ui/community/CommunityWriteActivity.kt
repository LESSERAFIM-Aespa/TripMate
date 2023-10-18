package kr.sparta.tripmate.ui.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.databinding.ActivityCommunityWriteBinding
import kr.sparta.tripmate.ui.community.main.CommunityListAdapter
import kr.sparta.tripmate.ui.community.main.CommunityModel
import kr.sparta.tripmate.ui.viewmodel.community.CommunityViewModel

class CommunityWriteActivity : AppCompatActivity() {

    private val viewModel: CommunityViewModel by viewModels()

    private lateinit var binding: ActivityCommunityWriteBinding

    val dataModelList = mutableListOf<CommunityModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newData = mutableListOf<CommunityModel>()// newData에 데이터를 추가
        val adapter = CommunityListAdapter(dataModelList)
        val database = Firebase.database
        val myRef = database.getReference("CommunityData")


        viewModel.updateDataModelList(newData) // ViewModel을 통해 데이터를 업데이트


        binding = ActivityCommunityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityWriteBackbutton.setOnClickListener {
            finish() //백버튼을 누를시 현재 액티비티를 종료하도록 설정
        }
        binding.communityWriteIcShare.setOnClickListener {


            val body_write = binding.communityWriteDescription.text.toString()
            val title_write = binding.communityWriteTitle.text.toString()
            // 파이어베이스로 저장하기 위한 설정및 함수호출
            val database = Firebase.database
            val myRef =
                database.getReference("CommunityData").child(Firebase.auth.currentUser!!.uid)
            val model = CommunityModel(
                id = "게시글 ID",
                thumbnail = null, // null 또는 이미지 URL
                title = title_write, // title_write 변수의 값
                body = body_write, // body_write 변수의 값
                profileNickname = "프로필 닉네임",
                profileThumbnail = null, // null 또는 프로필 이미지 URL
                views = "조회수",
                likes = "좋아요 수"
            )
            myRef
                .push()
                .setValue(model)
                .addOnSuccessListener {
                    // 데이터가 성공적으로 저장될 때 RecyclerView 업데이트
                    val newData = mutableListOf<CommunityModel>() // 새 데이터를 가져오는 코드가 필요
                    viewModel.updateDataModelList(newData) // ViewModel을 통해 데이터를 업데이트
                } // // 파이어베이스로 저장하기 위한 설정및 함수호출(끝)
            Toast.makeText(this, "글이 게시되었습니다(저장완료)", Toast.LENGTH_LONG).show()
        }
    }
}