package kr.sparta.tripmate.ui.viewmodel.community

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.ui.community.main.CommunityModel

class CommunityViewModel : ViewModel() {
    private val _dataModelList = MutableLiveData<MutableList<CommunityModel>>()
    val dataModelList: LiveData<MutableList<CommunityModel>> get() = _dataModelList
    private val _isLoading =
        MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun updateDataModelList() = viewModelScope.launch {
        kotlin.runCatching {
            _isLoading.value =
                true
            loadCommunity()
            _isLoading.value = false
        }
    }

    private fun loadCommunity() {
        val database = Firebase.database
        val myRef = database.getReference("CommunityData")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allCommunityData = mutableListOf<CommunityModel>()

                for (userPostSnapshot in snapshot.children) {
                    val postModel = userPostSnapshot.getValue(CommunityModel::class.java)
                    if (postModel != null) {
                        allCommunityData.add(postModel)
                    }
                }
                if (!allCommunityData.isNullOrEmpty()) {
                    _dataModelList.value = allCommunityData
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 오류 처리
            }
        })
    }

    fun updateCommuIsLike(model: CommunityModel, position: Int) {
        //or.Empty() : 해당값이 null일때 빈 리스트를 반환해준다.
        val list = dataModelList.value.orEmpty().toMutableList()
        list.find { it.key == model.key } ?: return

        list[position] = model

        val currentLikes = list[position].likes?.toIntOrNull() ?: 0
        val newLikes = if (list[position].commuIsLike) {
            currentLikes + 1
        } else {
            if (currentLikes >= 1) {
                currentLikes - 1
            } else {
                currentLikes
            }
        }
        list[position].likes = newLikes.toString()
        _dataModelList.value = list


        val commuDatabase = Firebase.database

        val updateList = arrayListOf<CommunityModel>()
        list?.let {
            updateList.addAll(it)
        }
        val commuRef = commuDatabase.getReference("CommunityData")
        commuRef.setValue(updateList)
    }
}