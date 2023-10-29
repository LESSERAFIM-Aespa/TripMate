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
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.data.model.community.BoardKeyModel
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.data.model.community.KeyModel
import kr.sparta.tripmate.data.model.login.UserData
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.model.firebase.toEntity
import kr.sparta.tripmate.domain.model.login.UserDataEntity
import kr.sparta.tripmate.domain.model.login.toEntity
import kr.sparta.tripmate.util.method.shortToast
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

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
     * 작성자: 서정한
     * 내용: 게시글 업로드시 사용할 키를 반환함.
     * */
    fun getCommunityKey(): String {
        val databaseRef = fireDatabase.getReference("CommunityData")
        return databaseRef.push().toString()
    }

    /**
     * 작성자: 서정한
     * 내용: 커뮤니티의 기존글 내용 업데이트
     * */
    fun updateCommunityWrite(item: CommunityModelEntity) {
        val databaseRef = fireDatabase.getReference("CommunityData")
        databaseRef.get().addOnSuccessListener { result ->
            val list = ArrayList<CommunityModel>()
            for (data in result.children) {
                val itemModel = data.getValue(CommunityModel::class.java)
                itemModel?.let { it -> list.add(it) }
            }
            val model = item.toCommunity()
            val findMyPost: CommunityModel? = list.find { it.key == model.key }

            // 기존 글 업데이트
            if (findMyPost != null) {
                val index = list.indexOf(findMyPost)
                list[index] = item.toCommunity()
                databaseRef.setValue(list)
                return@addOnSuccessListener
            }

            // 새로운 글 업로드
            list.add(model)
            databaseRef.setValue(list)
        }
    }

    /**
     * 작성자 : 박성수
     * 내용 : 북마크된 데이터를 식별합니다.
     * 키를 라이브데이터로 관리하고, RDB에 저장합니다.
     */
    fun saveFirebaseBookMarkData(
        model: CommunityModelEntity, uid: String, context: Context, communityLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, boardKeyLiveData:
        MutableLiveData<List<BoardKeyModelEntity?>>
    ) {
        val list = communityLiveData.value.orEmpty().toMutableList()
        list.find { it?.key == model.key } ?: return


        val myCommuRef = fireDatabase.getReference("MyBoardKey").child(uid)
        val myBoardKeyModel = BoardKeyModel(
            model.id, model.key,
            model.boardIsLike
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
    fun saveFirebaseLikeData(
        model: CommunityModelEntity, commuLiveData:
        MutableLiveData<List<CommunityModelEntity?>>, keyLiveData
        : MutableLiveData<List<KeyModelEntity?>>, uid: String
    ) {
        //or.Empty() : 해당값이 null일때 빈 리스트를 반환해준다.
        val list = commuLiveData.value.orEmpty().toMutableList()
        val mycommuRef = fireDatabase.getReference("MyKey").child(uid)
        val selectedIndex = list.indexOfFirst { it?.key == model.key }

        if (selectedIndex != -1) {
            list[selectedIndex] = model
        }

        val myKeyModel =
            KeyModel(model.id, model.key, model.commuIsLike)
        val myKeyList = keyLiveData.value.orEmpty().toMutableList()
        val selectedKey =
            myKeyList.find { it?.key == myKeyModel.key }  //내가선택한 아이템의 키하고 라이브데이터의 키하고 비교해서 라이브데이터 키가있는지 찾는다.

        if (selectedKey != null) {  //만약에 라이브데이터에 키에 내가 선택한 아이템의 키가 있어 ( 내가 아이템을 좋아요를 한적이 있다)
            selectedKey.myCommuIsLike =
                myKeyModel.myCommuIsLike    //그르면 라이브데이터를 키를 내가 선택한 아이템 키의 데이터로 바꾼다
        } else {
            myKeyList.add(myKeyModel.toEntity())
        }
        mycommuRef.setValue(myKeyList)
        keyLiveData.postValue(myKeyList)

        val commuRef = fireDatabase.getReference("CommunityData")
        val updateList = arrayListOf<CommunityModel>()
        list.forEach {                      //라이브데이터의 데이터를 새로운 리스트에 담는다.
            val updatedModel = it?.copy()
            updatedModel?.commuIsLike = false
            updateList.add(updatedModel!!.toCommunity())
        }
        commuRef.setValue(updateList)
        commuLiveData.postValue(list)
    }

    /**
     * 작성자 : 박성수
     * 내용 : RDB에 저장된 Community데이터를 가져옵니다.
     *
     */
    fun getFirebaseBoardData(
        uid: String,
        boardLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        likeKeyLiveData: MutableLiveData<List<KeyModelEntity?>>
    ) {
        val boardRef = fireDatabase.getReference("CommunityData")

        boardRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val getBoardList = snapshot.children.map {
                        it.getValue(CommunityModel::class.java)
                    }
                    getFirebaseLikeData(
                        uid,
                        getBoardList.toEntity(),
                        boardLiveData,
                        likeKeyLiveData
                    )
                } else boardLiveData.value = listOf()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    /**
     * 작성자 : 박성수
     * 내용 : CommunityData에 저장만하는 용도입니다.
     */
    fun saveFirebaseBoardData(
        model: CommunityModelEntity, boardLiveData:
        MutableLiveData<List<CommunityModelEntity?>>
    ) {
        val boardRef = fireDatabase.getReference("CommunityData")
        val modelList = boardLiveData.value.orEmpty().toMutableList()
        val selectIndex = modelList.indexOfFirst { it?.key == model.key }

        if (selectIndex != -1) {
            modelList[selectIndex] = model
            boardRef.setValue(modelList)
        }
        boardLiveData.value = modelList
    }

    /**
     * 작성자 : 박성수
     * 즐겨찾기를 업데이트 합니다.
     * CommunityData를 받아온 후 호출합니다.
     */
    fun getFirebaseLikeData(
        uid: String,
        communityData: List<CommunityModelEntity?>,
        communityLiveData: MutableLiveData<List<CommunityModelEntity?>>,
        likeKeyLiveData: MutableLiveData<List<KeyModelEntity?>>
    ) {

        val keyRef = fireDatabase.getReference("MyKey").child(uid)
        val keyList = mutableListOf<KeyModel>()

        keyRef.get().addOnSuccessListener {
            it.children.forEach { data ->
                val getKey = data.getValue(KeyModel::class.java)
                getKey?.let {
                    keyList.add(getKey)
                }
            }
            if (!keyList.isNullOrEmpty()) {
                val updateList = communityData.map { updateListData ->
                    val updatedModel = updateListData?.copy()
                    for (item in keyList) {
                        if (updateListData?.key == item.key) {
                            updatedModel?.commuIsLike = item.myCommuIsLike
                        }
                    }
                    updatedModel
                }
                communityLiveData.postValue(updateList)
                likeKeyLiveData.postValue(keyList.toEntity())
            } else {
                communityLiveData.postValue(communityData)
                likeKeyLiveData.postValue(keyList.toEntity())
            }
        }
    }

    /**
     * 작성자 : 박성수
     * 내용 : BoardKey를 이용해서 내가 북마크했던 아이템을 RDB에서 식별하고,
     * 북마크 데이터만 골라냅니다.
     */

    fun getFirebaseBookMarkData(
        uid: String,
        boardKeyLiveData: MutableLiveData<List<BoardKeyModelEntity?>>
    ) {
        val boardRef = fireDatabase.getReference("MyBoardKey").child(uid)
        val boardKeyList = arrayListOf<BoardKeyModel>()
        boardRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val getBoardKeyList = item.getValue(BoardKeyModel::class.java)

                    getBoardKeyList?.let {
                        boardKeyList.add(it)
                    }
                }
                if (!boardKeyList.isNullOrEmpty()) {
                    boardKeyLiveData.postValue(boardKeyList.toEntity())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    /**
     * 작성자 : 박성수
     * 내용 : 원래 유저데이터랑 저장하는부분이랑 합치려고했는데,
     * Login에서 쓸 꺼같애서 구분해놨습니다.
     * RDB에서 USER데이터만 들고옵니다.
     */
    fun updateUserData(
        uid: String,
        userLiveData: MutableLiveData<UserDataEntity?>
    ) {
        val userRef = fireDatabase.getReference("UserData").child(uid)
        userRef.get().addOnSuccessListener {
            val getUser = it.getValue(UserData::class.java)
            userLiveData.postValue(getUser?.toEntity())
        }
    }

    /**
     * 작성자 : 박성수
     * 내용 : 자기소개를 수정하고 저장 후 업데이트 합니다.
     * 추후 프로필 사진을 수정하거나 다른 데이터를 수정해도 재사용 가능합니다.
     */
    fun saveUserData(
        model: UserDataEntity, context: Context, userLiveData:
        MutableLiveData<UserDataEntity?>
    ) {
        val userRef = fireDatabase.getReference("UserData").child(model.login_Uid!!)
        userLiveData.postValue(model)
        userRef.setValue(model)
    }

    fun resignUserData(context: Context) {
        val userRef = fireDatabase.getReference("UserData").child(SharedPreferences.getUid(context))
        userRef.removeValue().addOnSuccessListener {
            context.shortToast("정상적으로 탈퇴 되었습니다.")
        }
    }

    private fun bookMarkToast(context: Context, selectedBoardKey: Boolean) {
        if (selectedBoardKey) {
            context.shortToast("북마크에 추가 되었습니다.")
        } else context.shortToast("이미 북마크된 목록 입니다.")
    }
}