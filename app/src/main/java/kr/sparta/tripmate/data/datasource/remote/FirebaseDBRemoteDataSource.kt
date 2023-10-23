package kr.sparta.tripmate.data.datasource.remote

import android.os.Build
import android.text.Html
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.firebase.toEntity

/**
 * 작성자: 서정한
 * 내용: Firebase RealtimeDatabase에서 필요한 자료를
 * 요청하고 응답받는 DataSource Class
 * */
class FirebaseDBRemoteDataSource {
    private val fireDatabase = Firebase.database
    fun getScrapedData(uid: String, liveData: MutableLiveData<List<ScrapEntity?>>) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("scrapData").child(uid).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val list = it.children.map {
                        it.getValue(ScrapModel::class.java)
                    }
                    liveData.postValue(list.toList().toEntity())
                }else {
                    liveData.postValue(listOf())
                }
            }

    }

    /**
     * 작성자: 서정한
     * 내용: 스크렙페이지에서 검색한 결과 중 사용자가 좋아요를 클릭했을경우
     * Firebase RDB에 저장한다.
     * */
    fun saveScrap(uid: String, model: ScrapModel) {
        val database = FirebaseDatabase.getInstance().reference

        /**
         * 작성자: 서정한
         * 내용: 스크랩데이터를 네이버에서 받아올때 html태그가 String에 섞여있음.
         * 검색한 데이터의 String값만 뽑아내기위한 메서드
         * */
        fun stripHtml(html: String): String {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                return Html.fromHtml(html).toString()
            }
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        }

        /**
         * 작성자: 서정한
         * 내용: Firebase RDB에서 받아온 snapshot을
         * ScrapEntity로 변환한 list를 반환함
         * */
        fun convertToModels(snapshot: DataSnapshot): ArrayList<ScrapModel> {
            val list = ArrayList<ScrapModel>()
            for (item in snapshot.children) {
                val model = item.getValue(ScrapModel::class.java)
                model?.let { list.add(it) }
            }
            return list
        }

        val ref = database.child("scrapData").child(uid)
        ref.get()
            .addOnSuccessListener {
                val list = ArrayList<ScrapModel>()
                list.addAll(convertToModels(it))
                val check = list.filter { it.url == model.url }
                if (!it.exists() || check.isEmpty()) {
                    list.add(
                        model.copy(
                            title = stripHtml(model.title),
                            description = stripHtml(model.description)
                        )
                    )
                    ref.setValue(list)
                    list.clear()
                    return@addOnSuccessListener
                }
            }
    }

    /**
     * 작성자: 서정한
     * 내용: 스크렙페이지에서 검색한 결과 중 사용자가 좋아요를 해지했을경우
     * 해당 item을 Firebase RDB에서 삭제한다.
     * */
    fun removeScrap(uid: String, model: ScrapEntity) {
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("scrapData").child(uid)

        /**
         * 작성자: 서정한
         * 내용: Firebase RDB에서 받아온 snapshot을
         * 순회하며 해당 item을 DB에 remove 요청.
         * */
        database.child("scrapData").child(uid).get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val serverModel = item.getValue(ScrapEntity::class.java)
                    if (serverModel?.url == model.url) {
                        item.ref.removeValue()
                        break
                    }
                }
            }
    }
    /**
     *  작성자: 박성수
     *  내용 : Firebase RDB에서 받아온 Community의 공용데이터를
     *  allCommunityData리스트에 담아서 라이브데이터와 ui를 함께
     *  myLoadCommunity함수로 넘깁니다.
     */
    fun getCommunityData(
        uid: String, commuLiveData: MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>
    ) {
        Log.d("TripMates", "getCommunityData호출은 되냐?")
        val comuRef = fireDatabase.getReference("CommunityData")
        comuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allCommunityData = arrayListOf<CommunityModel>()

                for (userPostSnapshot in snapshot.children) {
                    val postModel = userPostSnapshot.getValue(CommunityModel::class.java)
                    if (postModel != null) {
                        allCommunityData.add(postModel)
                    }
                }
                if (!allCommunityData.isNullOrEmpty()) {
                    myLoadCommunity(allCommunityData, commuLiveData, keyLiveData, uid)
                }
                Log.d("TripMates","getCommunityData의 allCommunityData ${allCommunityData}")
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}