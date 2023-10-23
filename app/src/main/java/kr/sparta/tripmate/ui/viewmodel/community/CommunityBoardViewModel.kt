package kr.sparta.tripmate.ui.viewmodel.community

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.util.method.shortToast

class CommunityBoardViewModel : ViewModel(){
    private val _boardResult = MutableLiveData<MutableList<CommunityModelEntity>>()
    val boardResult: LiveData<MutableList<CommunityModelEntity>> get() = _boardResult

    fun savedBoard(model: CommunityModelEntity, position: Int, context: Context) {

        val commuDatabase = Firebase.database
        val boardRef = commuDatabase.getReference("BoardData")
        boardRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val boardList = arrayListOf<CommunityModelEntity>()

                for(item in snapshot.children){
                    val getBordList = item.getValue(CommunityModelEntity::class.java)
                    getBordList?.let{
                        boardList.add(it)
                    }
                }
                val isDuplicate = boardList.any { it.key == model.key }
                if(!isDuplicate){
                    boardList.add(model)
                    boardRef.setValue(boardList)
                } else context.shortToast("이미 북마크에 추가 된 목록입니다.")
                _boardResult.value = boardList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        } )
    }
}