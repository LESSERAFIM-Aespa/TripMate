package kr.sparta.tripmate.ui.community

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityCommunityWriteBinding
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.ui.viewmodel.community.CommunityViewModel
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences
import java.io.ByteArrayOutputStream


/**
 * 작성자 : 박성수
 * 1~14까지 확인하시고 이상한거 있으면 말씀해주세요
 * 기존의 코드는 주석처리해놨습니다. 확인하시고 말씀해주시면 삭제하겠습니다.
 */
class CommunityWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityWriteBinding
    private lateinit var commu_Database: DatabaseReference         //1. 데이터베이스 객체 생성
    private val storage = Firebase.storage
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommunityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        commu_Database = Firebase.database.reference                        //2. 데이터베이스 객체 초기화

        communityBackBtn()      //3. 뒤로가기 버튼
        communityAddImage()
        communitySaveBtn()     //4. 게시하기 버튼
    }

    private fun communitySaveBtn() {
        binding.communityWriteIcShare.setOnClickListener {
            if (imageUrl == null) {        // imageURL 이 없을때 처리
                handleNoImageAdded()
            } else {                       // imageURL 이 생성되었을때 처리
                continueWithSave()
            }
        }
    }

    private fun continueWithSave() {
        //9. 데이터베이스 경로소ㅑ
        val bodyWrite = binding.communityWriteDescription.text.toString()
        val titleWrite = binding.communityWriteTitle.text.toString()
        val uid = SharedPreferences.getUid(this)                 //5. sharedpreferences에 저장된 uid
        val myRef = commu_Database.child("CommunityData")
        val nickName = SharedPreferences.getNickName(this)          //6. sharedpreferences에 저장된 닉네임
        val profile =
            SharedPreferences.getProfile(this)            //7.sharedpreferences에 저장된 프로필 사진
        val key = myRef.push()

        val imageRef = storage.reference.child("$titleWrite.jpg")
        val imageView = binding.communitiyWriteAddimage

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
        }
        imageRef.putBytes(data).addOnSuccessListener { taskSnapshot ->

            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                saveToDatabase(uid, titleWrite, bodyWrite, nickName, profile, key, imageUrl)

            }
        }
    }

    private fun handleNoImageAdded() {               // 이미지를 추가 하지 않았을때의 처리 방식
        val bodyWrite = binding.communityWriteDescription.text.toString()
        val titleWrite = binding.communityWriteTitle.text.toString()
        val uid = SharedPreferences.getUid(this) // 5. sharedpreferences에 저장된 uid
        val myRef = commu_Database.child("CommunityData")
        val nickName = SharedPreferences.getNickName(this) // 6. sharedpreferences에 저장된 닉네임
        val profile = SharedPreferences.getProfile(this) // 7.sharedpreferences에 저장된 프로필 사진
        val key = myRef.push()

        val imageUrl = ""
        saveToDatabase(uid, titleWrite, bodyWrite, nickName, profile, key, imageUrl)
    }

    private fun saveToDatabase(
        uid: String,
        titleWrite: String,
        bodyWrite: String,
        nickName: String,
        profile: String,
        key: DatabaseReference,
        imageUrl: String
    ) {
        val writeModel = CommunityModel(
            uid,
            titleWrite,
            bodyWrite,
            nickName,
            profile,
            "0",
            "0",
            key.toString(),
            imageUrl
        )

        val myRef = commu_Database.child("CommunityData")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val commuList = ArrayList<CommunityModel>()

                for (items in snapshot.children) {
                    val getcommuModel = items.getValue(CommunityModel::class.java)
                    getcommuModel?.let {
                        commuList.add(it)
                    }
                }
                commuList.add(writeModel)
                myRef.setValue(commuList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        shortToast("글이 게시되었습니다(저장완료)")
        finish()
    }


    private fun communityBackBtn() {
        binding.communityWriteBackbutton.setOnClickListener {
            finish() //백버튼을 누를시 현재 액티비티를 종료하도록 설정
        }
    }

    private fun communityAddImage() {
        binding.communitiyWriteAddimage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)

        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            binding.communitiyWriteAddimage.setImageURI(selectedImageUri)
            handleImageselected(selectedImageUri)
        }
    }

    private fun handleImageselected(selectedImageUri: Uri?) {  // imageURL 을 생성할 지 말지
        if (selectedImageUri != null) {       // addImage가 활성화 될때 imageURL이 생성 됨
            imageUrl = selectedImageUri.toString()
        } else {                              // addImage가 비활성화 일때는 ""로 받아옴.
            imageUrl = ""
        }
    }
}