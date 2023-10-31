package kr.sparta.tripmate.data.datasource.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.KeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.model.firebase.toEntity

/**
 * 작성자: 서정한
 * 내용: Firebase RealtimeDatabase에서 필요한 자료를
 * 요청하고 응답받는 DataSource Class
 * */
class FirebaseBoardRemoteDataSource {
    private val fireDatabase = Firebase.database
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
        Log.d("rewq","좋아요키 저장id ${uid}")
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
                        if (updateListData?.id == item.uid && updateListData?.key == item.key) {
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

}