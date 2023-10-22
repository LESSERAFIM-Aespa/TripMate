package kr.sparta.tripmate.ui.viewmodel.community

import android.content.Context
import android.util.Log
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
import kr.sparta.tripmate.ui.community.main.CommunityMyModel
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class CommunityViewModel : ViewModel() {
    private val _dataModelList = MutableLiveData<MutableList<CommunityModel>>()
    val dataModelList: LiveData<MutableList<CommunityModel>> get() = _dataModelList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _keyList = MutableLiveData<MutableList<CommunityMyModel>>()
    val keyList: LiveData<MutableList<CommunityMyModel>> get() = _keyList
    private val database = Firebase.database
    fun updateDataModelList(context: Context) = viewModelScope.launch {
        kotlin.runCatching {
            _isLoading.value = true
            loadCommunity(context)
            _isLoading.value = false
        }
    }

    private fun loadCommunity(context: Context) {
        val comuRef = database.getReference("CommunityData")
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
                    myLoadCommunity(allCommunityData, context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun myLoadCommunity(allCommunityData: ArrayList<CommunityModel>, context: Context) {
        val mycommuRef = database.getReference("MyKey").child(SharedPreferences.getUid(context))
        mycommuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val myKeyData = arrayListOf<CommunityMyModel>()

                for (item in snapshot.children) {
                    val getMyKey = item.getValue(CommunityMyModel::class.java)
                    if (getMyKey != null) {
                        myKeyData.add(getMyKey)
                    }
                }
                if (!myKeyData.isNullOrEmpty()) {
                    for (item in allCommunityData) {
                        for (myItem in myKeyData) {
                            if (item.id == myItem.uid && item.key == myItem.key) {
                                item.commuIsLike = myItem.myCommuIsLike
                            }
                        }
                    }
                }
                _keyList.value = myKeyData
                _dataModelList.value = allCommunityData

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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
    fun updateCommuIsLike(model: CommunityModel, position: Int, context: Context) {
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
        val mycommuRef = database.getReference("MyKey").child(SharedPreferences.getUid(context))
        val myKeyModel =
            CommunityMyModel(list[position].id, list[position].key, list[position].commuIsLike)
        val myKeyList = keyList.value.orEmpty().toMutableList()
        val selectedKey = myKeyList.find { it.key == myKeyModel.key }
        if (selectedKey != null) {
            selectedKey.myCommuIsLike = myKeyModel.myCommuIsLike
        } else {
            myKeyList.add(myKeyModel)
        }
        mycommuRef.setValue(myKeyList)
        _keyList.value = myKeyList

        list[position].likes = newLikes.toString()

        val commuRef = database.getReference("CommunityData")
        val updateList = arrayListOf<CommunityModel>()
        list?.let {
            updateList.addAll(it)
            updateList.forEach { it.commuIsLike = false }
        }
        commuRef.setValue(updateList)
        _dataModelList.value = list
    }
    fun updateCommuView(model: CommunityModel, position: Int) {
        val items = dataModelList.value.orEmpty().toMutableList()
        val selectedItem = items.find { it.key == model.key } ?: return
        items[position] = model
        val currentViews = selectedItem.views?.toIntOrNull() ?: 0
        val newViews = currentViews + 1

        items[position].views = newViews.toString()
        Log.d("TripMates", "뷰 : ${items[position].views}")

        val commuRef = database.getReference("CommunityData")
        val updateList = arrayListOf<CommunityModel>()
        items?.let {
            updateList.addAll(it)
        }
        commuRef.setValue(updateList)
        _dataModelList.value = items

    }
}