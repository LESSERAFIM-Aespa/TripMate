package kr.sparta.tripmate.ui.community

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kr.sparta.tripmate.databinding.ActivityCommunityWriteBinding
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


/**
 * 작성자 : 박성수
 * 1~14까지 확인하시고 이상한거 있으면 말씀해주세요
 * 기존의 코드는 주석처리해놨습니다. 확인하시고 말씀해주시면 삭제하겠습니다.
 */
class CommunityWriteActivity : AppCompatActivity() {
    companion object {
        const val MODEL_EDIT = "model_edit"
        const val EXTRA_ENTRY_TYPE = "extra_entry_type"

        fun newIntentForWrite(context: Context): Intent =
            Intent(context, CommunityWriteActivity::class.java).apply {
                putExtra(EXTRA_ENTRY_TYPE, ContentType.Write.name)
            }

        fun newIntentForEdit(context: Context, model: CommunityModelEntity): Intent =
            Intent(context, CommunityWriteActivity::class.java).apply {
                putExtra(MODEL_EDIT, model)
                putExtra(EXTRA_ENTRY_TYPE, ContentType.Edit.name)
            }
    }

    private val binding: ActivityCommunityWriteBinding by lazy {
        ActivityCommunityWriteBinding.inflate(layoutInflater)
    }

    private lateinit var commu_Database: DatabaseReference

    private val storage = Firebase.storage

    private val model by lazy {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MODEL_EDIT, CommunityModelEntity::class.java)
        } else {
            intent.getParcelableExtra(MODEL_EDIT)
        }
    }

    private val entryType by lazy {
        intent.getStringExtra(EXTRA_ENTRY_TYPE)
    }

    // 겔러리 이미지가져오기
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let { binding.communityWriteImage.setImageURI(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        commu_Database = Firebase.database.reference

        initView()
    }

    private fun initView() = with(binding) {
        when (entryType) {
            ContentType.Write.name -> {

            }

            ContentType.Edit.name -> {
                model?.let {
                    communityWriteTitle.setText(it.title)
                    communityWriteDescription.setText(it.description)
                    communityWriteImage.load(it.addedImage)
                }
            }
        }

        // 뒤로가기
        communityWriteToolbar.setNavigationOnClickListener {
            finish()
        }


        // 게시
        communityWritePost.setOnClickListener {
            /**
             * 작성자: 서정한
             * 내용: 타이틀 입력값 검증
             * */
            val titleValidate: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val titleRegex = Pattern.compile("^[a-zA-Z0-9ㄱ - | 가-힣]*$")
                    val matcher = s?.let { it1 -> titleRegex.matcher(it1) }
                    matcher?.let {
                        if(!it.find()){
                            Toast.makeText(this@CommunityWriteActivity, "제목은 영어, 한글, 숫자만 입력가능합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            }

            /**
             * 작성자: 서정한
             * 내용: 내용 입력값 검증
             * */
            val descriptionValidate: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                }

            }

            communityWriteTitle.addTextChangedListener(titleValidate)
            communityWriteDescription.addTextChangedListener(descriptionValidate)

            continueWithSave()
        }


        // 기기이미지 불러오기
        communityWriteImage.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            imageLauncher.launch(gallery)
        }

    }

    private fun continueWithSave() {
        //9. 데이터베이스 경로소ㅑ
        val bodyWrite = binding.communityWriteDescription.text.toString()
        val titleWrite = binding.communityWriteTitle.text.toString()
        val uid = SharedPreferences.getUid(this)
        val myRef = commu_Database.child("CommunityData")
        val nickName = SharedPreferences.getNickName(this)
        val profile =
            SharedPreferences.getProfile(this)
        val key = myRef.push()

        if (binding.communityWriteImage.drawable != null) {
            val imageRef = storage.reference.child("$titleWrite.jpg")
            val imageView = binding.communityWriteImage

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
        } else {
            val imageUrl = ""
            saveToDatabase(uid, titleWrite, bodyWrite, nickName, profile, key, imageUrl)
        }
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
            "",
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
}