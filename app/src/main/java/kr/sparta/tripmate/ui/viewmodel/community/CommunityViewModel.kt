package kr.sparta.tripmate.ui.viewmodel.community

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kr.sparta.tripmate.ui.community.main.CommunityModel

/**
 * 기존의 리스트를 통채로 받아와서 라이브데이터에 넣어줘서 결과값만 받았었는데
 * 데이터관리를 뷰모델에서 불러와서 관리하고 그걸 프래그먼트에서 관찰만 하는 방식으로 바꿨습니다.
 * 1~6번까지 확인하시고 이상한거 있으면 말씀해주세요
 * 기존의 코드는 주석처리해놨습니다. 확인하시고 말씀해주시면 삭제하겠습니다.
 */
class CommunityViewModel : ViewModel() {
    private val _dataModelList = MutableLiveData<MutableList<CommunityModel>>()
    val dataModelList: LiveData<MutableList<CommunityModel>> get() = _dataModelList
    private val _isLoading = MutableLiveData<Boolean>()             //1. 로딩바 추가 : 데이터를 로드중일때 프로그레스바가 화면에 표시됩니다.
    val isLoading: LiveData<Boolean> get() = _isLoading
    fun updateDataModelList()=viewModelScope.launch {
        kotlin.runCatching {            //2. 예외처리 하는데 좋다는데 잘 모르겠음 정한님께 물어보시면 됩니다.
            _isLoading.value = true     //3. updateDataModelList함수를 프래그먼트에서 호출하면(즉, 데이터 요청 시작하면 Loding을 true로 초기화
            loadCommunity()         //4. 파이어베이스에서 데이터를 로드하는 함수( 프래그먼트에서 고대로 가져옴)
        }
    }

    private fun loadCommunity(){
        val database = Firebase.database
        val myRef = database.getReference("CommunityData")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allCommunityData = mutableListOf<CommunityModel>()

                for (userPostSnapshot in snapshot.children) {
                    for (postSnapshot in userPostSnapshot.children) {
                        val postId = postSnapshot.key
                        val postModel = postSnapshot.getValue(CommunityModel::class.java)
                        if (postId != null && postModel != null) {
                            allCommunityData.add(postModel)
                        }
                    }
                }
                _dataModelList.value = allCommunityData     //5. 데이터베이스에서 받아온 데이터를 넣어줌
                _isLoading.value = false            //6. 검색해서 데이터를 모두 받아왔으므로 로딩을 false로 바꿔줌(검색끝났다고)
            }

            override fun onCancelled(error: DatabaseError) {
                // 오류 처리
            }
        })
    }
}