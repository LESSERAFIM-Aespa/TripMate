package kr.sparta.tripmate.ui.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityCommunityWriteBinding
import kr.sparta.tripmate.ui.community.main.CommunityListAdapter
import kr.sparta.tripmate.ui.community.main.CommunityModel
import kr.sparta.tripmate.ui.viewmodel.community.CommunityViewModel
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

/**
 * 작성자 : 박성수
 * 1~14까지 확인하시고 이상한거 있으면 말씀해주세요
 * 기존의 코드는 주석처리해놨습니다. 확인하시고 말씀해주시면 삭제하겠습니다.
 */
class CommunityWriteActivity : AppCompatActivity() {

    private val viewModel: CommunityViewModel by viewModels()
    private var commuThumbnail = R.drawable.emptycommu
    private lateinit var binding: ActivityCommunityWriteBinding
    private lateinit var commu_Database : DatabaseReference         //1. 데이터베이스 객체 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val allCommunityData = mutableListOf<CommunityModel>()
//        val adapter = CommunityListAdapter(dataModelList)
        val database = Firebase.database
        val myRef = database.getReference("CommunityData")


        viewModel.updateDataModelList(allCommunityData) // ViewModel을 통해 데이터를 업데이트

        binding = ActivityCommunityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        commu_Database = Firebase.database.reference                        //2. 데이터베이스 객체 초기화

        communityBackBtn()      //3. 뒤로가기 버튼
        communitySaveBtn()     //4. 게시하기 버튼
    }

    private fun communitySaveBtn() {
        //9. 데이터베이스 경로
        binding.communityWriteIcShare.setOnClickListener {
            val bodyWrite = binding.communityWriteDescription.text.toString()
            val titleWrite = binding.communityWriteTitle.text.toString()
            val uid = SharedPreferences.getUid(this)                 //5. sharedpreferences에 저장된 uid
            val myRef = commu_Database.child("CommunityData")
            val nickName = SharedPreferences.getNickName(this)          //6. sharedpreferences에 저장된 닉네임
            val profile = SharedPreferences.getProfile(this)            //7.sharedpreferences에 저장된 프로필 사진
            val key = myRef.push()
            val writeModel = CommunityModel(uid,commuThumbnail.toString(), titleWrite, bodyWrite, nickName,
                profile,"0","${key}") //8. 작성 목록
            // 파이어베이스로 저장하기 위한 설정및 함수호출

            myRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val commuList = ArrayList<CommunityModel>()     //10. 파이어베이스에 저장될 리스트

                    for (items in snapshot.children){   //11. 데이터베이스에 저장된 항목을 불러와서 10에서만든 리스트에 추가한다.
                        val getcommuModel = items.getValue(CommunityModel::class.java)
                        getcommuModel?.let{
                            commuList.add(it)
                        }
                    }
                    commuList.add(writeModel)   //12. 11에서 추가한 데이터에 내가 작성한 목록을 추가한다.
                    myRef.setValue(commuList)   //13. 11,12에서 추가된 데이터를 파이어베이스의 데이터베이스에 덮어씌운다.
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            shortToast("글이 게시되었습니다(저장완료)")
            finish()    //14. 저장했으면 커뮤니티 메인화면으로 넘어간다.
        }
    }

    private fun communityBackBtn() {
        binding.communityWriteBackbutton.setOnClickListener {
            finish() //백버튼을 누를시 현재 액티비티를 종료하도록 설정
        }
    }
}