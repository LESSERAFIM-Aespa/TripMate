package kr.sparta.tripmate.ui.viewmodel.scrap

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.scrap.toScrapEntity
import kr.sparta.tripmate.domain.usecase.GetSearchBlogUseCase
import kr.sparta.tripmate.domain.usecase.RemoveFirebaseScrapData
import kr.sparta.tripmate.domain.usecase.SaveFirebaseScrapData
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class ScrapViewModel(
    private val searchBlog: GetSearchBlogUseCase,
    private val saveFirebaseScrapData: SaveFirebaseScrapData,
    private val removeFirebaseScrapData: RemoveFirebaseScrapData
) : ViewModel() {
    private val _scrapResult = MutableLiveData<List<ScrapEntity>>()
    val scrapResult: LiveData<List<ScrapEntity>> get() = _scrapResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /**
     * 작성자: 박성수
     * 내용: 네이버 API로 블로그 검색한 결과를 받아옴
     * */
    fun searchAPIResult(q: String, context: Context) = viewModelScope.launch {
        kotlin.runCatching {
            // loading start
            _isLoading.value = true

            val result = searchBlog(q)
            val scrapItems = ArrayList<ScrapEntity>()
            result.items?.let {
                for (i in it.indices) {
                    scrapItems.add(it[i].toScrapEntity())
                }

                updateBookmarkedScrapData(context) { getScrapList ->
                    checkIsBookmarkedScrapData(scrapItems, getScrapList)
                    _scrapResult.value = scrapItems
                }
                // loading end
                _isLoading.value = false
            }
        }.onFailure {
            _isLoading.value = false
            Log.e("ScrapViewModel", it.message.toString())
        }
    }

    /**
     * 작성자: 박성수
     * 내용:검색데이터와 저장된데이터를 비교해서
     * 검색했을때 북마크 추가된 데이터들을 체크표시 해줍니다.
     * */
    private fun updateBookmarkedScrapData(
        context: Context,
        onSuccess: (List<ScrapEntity>) -> Unit
    ) {
        val scrapRef: DatabaseReference = Firebase.database.reference.child("scrapData")
            .child(SharedPreferences.getUid(context))
        scrapRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val getScrapList = ArrayList<ScrapEntity>()

                for (items in dataSnapshot.children) {
                    val loadScrapList = items.getValue(ScrapEntity::class.java)
                    loadScrapList?.let {
                        getScrapList.add(it)
                    }
                }
                onSuccess(getScrapList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TripMates", "Error reading data: $databaseError")
            }
        })
    }

    /**
     * 작성자: 박성수
     * 내용: Firebase RDB에서 가져온 Scrap목록을 기준으로
     * 북마크된 item들을 찾아줍니다
     * */
    private fun checkIsBookmarkedScrapData(scrapItems: List<ScrapEntity>, getScrapList: List<ScrapEntity>) {
        for (i in 0 until scrapItems.size) {
            val isDuplicate = getScrapList.any { it.url == scrapItems[i].url }
            if (isDuplicate) {
                scrapItems[i].isLike = true
                Log.d("TripMates", "저장했던데이터:${scrapItems[i].isLike}")
            }
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 스크랩 item을 Firebase RDB에 저장합니다.
     * */
    fun saveScrap(uid: String, model: ScrapEntity) = saveFirebaseScrapData.invoke(uid, model)

    /**
     * 작성자: 서정한
     * 내용: Firebase RDB에 저장된 아이템에서 선택한 스크랩 item을 찾아 삭제합니다.
     * */
    fun removeScrap(uid:String, model: ScrapEntity) = removeFirebaseScrapData.invoke(uid, model)

    fun updateIsLike(model: ScrapEntity, position: Int) {
        //or.Empty() : 해당값이 null일때 빈 리스트를 반환해준다.
        val list = scrapResult.value.orEmpty().toMutableList()
        list.find { it.url == model.url } ?: return
        list[position] = model
        _scrapResult.value = list
    }
}