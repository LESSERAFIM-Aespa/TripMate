package kr.sparta.tripmate.data.datasource.remote

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.data.model.community.BoardKeyModel
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.data.model.community.KeyModel
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.model.firebase.toEntity
import kr.sparta.tripmate.util.method.shortToast

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
                } else {
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
     *  allCommunityData리스트에 담아서 라이브데이터(데이터+키)와 ui를 함께
     *  myLoadCommunity함수로 넘깁니다.
     */
    fun getCommunityData(
        uid: String, commuLiveData: MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>
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
                    myBoardLoadCommunity(allCommunityData, commuLiveData, boardKeyLiveData, uid)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    /**
     * 작성자 : 박성수
     * 내용 : 북마크관련 식별하기위해 키 업데이트& 저장
     */
    private fun myBoardLoadCommunity(
        allCommunityData: ArrayList<CommunityModel>,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        boardKeyLiveData
        : MutableLiveData<List<BoardKeyModelEntity?>>, uid: String
    ) {
        val mycommuRef = fireDatabase.getReference("MyBoardKey").child(uid)
        mycommuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val myKeyDateModels = arrayListOf<BoardKeyModel>()
                for (item in snapshot.children) {
                    val getMyKeyModel = item.getValue(BoardKeyModel::class.java)
                    getMyKeyModel?.let {
                        myKeyDateModels.add(it)
                    }
                }
                if (!myKeyDateModels.isNullOrEmpty()) {
                    val updatedCommunityData = allCommunityData.map {
                        val updateModel = it.copy()
                        for (myItem in myKeyDateModels) {
                            if (it.id == myItem.uid && it.key == myItem.key) {
                                updateModel.boardIsLike = myItem.myBoardIsLike
                            }
                        }
                        updateModel
                    }
                    boardKeyLiveData.postValue(myKeyDateModels.toEntity())
                } else {
                    boardKeyLiveData.postValue(myKeyDateModels.toEntity())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    /**
     * 작성자 : 박성수
     * 내용 :  Firebase RDB에서 키 데이터(내가 좋아요를 추가한 데이터를 공용데이터에서 식별하기 위한 키)를 받아옵니다.
     * 키를 이용해서 공용데이터에서 내가 좋아요를 추가한 데이터를 식별해서 해당 데이터의 좋아요 버튼을 true로 바꿔줍니다.
     * 커뮤니티 공용데이터와 라이브데이터(데이터+키)를 getCommunityData에서 넘겨받아와서 업데이트된 데이터와 키를 저장해줍니다.
     *
     */
    private fun myLoadCommunity(
        allCommunityData: ArrayList<CommunityModel>,
        commuLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, uid: String
    ) {
        val mycommuRef = fireDatabase.getReference("MyKey").child(uid)
        mycommuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val myKeyDateModels = arrayListOf<KeyModel>()

                for (item in snapshot.children) {
                    val getMyKeyModel = item.getValue(KeyModel::class.java)
                    if (getMyKeyModel != null) {
                        myKeyDateModels.add(getMyKeyModel)
                    }
                }
                if (!myKeyDateModels.isNullOrEmpty()) {
                    val updatedCommunityData = allCommunityData.map { communityModel ->
                        val updatedModel = communityModel.copy()
                        for (myItem in myKeyDateModels) {
                            if (communityModel.id == myItem.uid && communityModel.key == myItem.key) {
                                updatedModel.commuIsLike = myItem.myCommuIsLike
                            }
                        }
                        updatedModel
                    }
                    commuLiveData.postValue(updatedCommunityData.toEntity())
                    keyLiveData.postValue(myKeyDateModels.toEntity())
                } else {
                    commuLiveData.postValue(allCommunityData.toEntity())
                    keyLiveData.postValue(myKeyDateModels.toEntity())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    /**
     * 작성자 : 박성수
     * 내용 : 북마크된 데이터를 식별합니다.
     * 라이브데이터로 저장하고,
     */
    fun updateCommuBoard(
        model: CommunityModel, position: Int, communityLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>, uid: String, context: Context
    ) {
        val list = communityLiveData.value.orEmpty().toMutableList()
        list.find { it?.key == model.key } ?: return
        list[position] = model.toEntity()

        val myCommuRef = fireDatabase.getReference("MyBoardKey").child(uid)
        val myBoardKeyModel = BoardKeyModel(
            list[position]!!.id, list[position]!!.key,
            list[position]!!.boardIsLike
        )
        val myBoardKeyList = boardKeyLiveData.value.orEmpty().toMutableList()
        val selectedBoardKey = myBoardKeyList.find { it?.key == myBoardKeyModel.key }
        if (selectedBoardKey != null) {
            selectedBoardKey.myBoardIsLike = myBoardKeyModel.myBoardIsLike
            bookMarkToast(context, selectedBoardKey.myBoardIsLike)
        } else {
            myBoardKeyList.add(myBoardKeyModel.toEntity())
        }
        myCommuRef.setValue(myBoardKeyList)
        boardKeyLiveData.postValue(myBoardKeyList)
        communityLiveData.postValue(list)
    }

    /**
     * 작성자 : 박성수
     * 내용 : 위에서 업데이트된 라이브데이터를 이용해서 내가 좋아요버튼을 눌렀을때 좋아요횟수를 1추가시킵니다.
     * 내가 좋아요 버튼을 누른 데이터를 식별할 키를 저장합니다.
     * 모든 데이터의 좋아요버튼을 false로 저장합니다.
     * 라이브데이터를 업데이트하고 RDB에 저장합니다.
     */
    fun updateCommuIsLike(
        model: CommunityModel, position: Int, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, uid: String
    ) {
        //or.Empty() : 해당값이 null일때 빈 리스트를 반환해준다.
        val list = commuLiveData.value.orEmpty().toMutableList()
        list.find { it?.key == model.key } ?: return

        list[position] = model.toEntity()

        val currentLikes = list[position]!!.likes?.toIntOrNull() ?: 0
        val newLikes = if (list[position]!!.commuIsLike) {
            currentLikes + 1
        } else {
            if (currentLikes >= 1) {
                currentLikes - 1
            } else {
                currentLikes
            }
        }
        list[position]!!.likes = newLikes.toString()

        val mycommuRef = fireDatabase.getReference("MyKey").child(uid)
        val myKeyModel =
            KeyModel(
                list[position]!!.id, list[position]!!.key,
                list[position]!!.commuIsLike
            )
        val myKeyList = keyLiveData.value.orEmpty().toMutableList()
        val selectedKey = myKeyList.find { it?.key == myKeyModel.key }
        if (selectedKey != null) {
            selectedKey.myCommuIsLike = myKeyModel.myCommuIsLike
        } else {
            myKeyList.add(myKeyModel.toEntity())
        }
        mycommuRef.setValue(myKeyList)
        keyLiveData.postValue(myKeyList)

        val commuRef = fireDatabase.getReference("CommunityData")
        val updateList = arrayListOf<CommunityModel>()
        list.forEach {
            val updatedModel = it?.copy()
            updatedModel?.commuIsLike = false
            updateList.add(updatedModel!!.toCommunity())
        }
        commuRef.setValue(updateList)
        commuLiveData.postValue(list)
    }

    /**
     * 작성자 : 박성수
     * 내용 : 게시판을 클릭했을때 해당 게시판으니 조회수가 1증가합니다.
     * 라이브데이터에 업데이트하고 RDB에 저장합니다.
     */
    fun updateCommuView(
        model: CommunityModel, position: Int, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>
    ) {
        val list = commuLiveData.value.orEmpty().toMutableList()
        val selectedItem = list.find { it?.key == model.key } ?: return
        list[position] = model.toEntity()
        val currentViews = selectedItem.views?.toIntOrNull() ?: 0
        val newViews = currentViews + 1

        list[position]?.views = newViews.toString()
        val commuRef = fireDatabase.getReference("CommunityData")
        val updateList = arrayListOf<CommunityModel>()
        list.forEach {
            val updatedModel = it?.copy()
            updateList.add(updatedModel!!.toCommunity())
        }
        commuRef.setValue(updateList)
        commuLiveData.postValue(list)
    }

    private fun bookMarkToast(context: Context, selectedBoardKey: Boolean) {
        if (selectedBoardKey) {
            context.shortToast("북마크에 추가 되었습니다.")
        } else context.shortToast("이미 북마크된 목록 입니다.")
    }
}