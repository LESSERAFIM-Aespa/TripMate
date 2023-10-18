package kr.sparta.tripmate.data.repository.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity

class HomeFirstRepositoryImpl() {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    fun getData(uid:String): LiveData<MutableList<ScrapEntity>> {
        val firstData = MutableLiveData<MutableList<ScrapEntity>>()

        val dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    if (snapshot.exists()) {
                        val homeScrapDataList = mutableListOf<ScrapEntity>()
                        for (dataSnapshot in snapshot.children) {
                            val homeScrapData = dataSnapshot.getValue(ScrapEntity::class.java)
                            homeScrapData?.let { homeScrapDataList.add(it) }
                        }
                        firstData.value = homeScrapDataList
                    } else firstData.value = mutableListOf()
                } catch(e: Exception){
                    firstData.value = mutableListOf()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                firstData.value = mutableListOf()
            }
        }

        databaseReference.child("scrapData").child(uid).addValueEventListener(dataListener)

        return firstData
    }
}