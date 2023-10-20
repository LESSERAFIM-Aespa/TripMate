package kr.sparta.tripmate.data.datasource.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.firebase.toEntity

/**
 * 작성자: 서정한
 * 내용: Firebase RealtimeDatabase에서 필요한 자료를
 * 요청하고 응답받는 DataSource Class
 * */
class FirebaseDBRemoteDataSource {
    private val database = FirebaseDatabase.getInstance().reference

    fun getScrapedData(uid: String, liveData: MutableLiveData<List<ScrapEntity>>) {
        database.child("scrapData").child(uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val list = snapshot.children.map{
                            it.getValue(ScrapModel::class.java)
                        }
                        liveData.postValue(list.toEntity())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TripMate_GetFirebaseDatabaseRemoteSource", error.toString())
                }
            })
    }

}