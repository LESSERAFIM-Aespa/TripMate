package kr.sparta.tripmate.ui.viewmodel.scrap

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.ScrapModel
import kr.sparta.tripmate.domain.model.toScrapModel
import kr.sparta.tripmate.domain.usecase.GetSearchBlogUseCase

class ScrapViewModel(private val searchBlog: GetSearchBlogUseCase) : ViewModel() {
    private val _gourResult = MutableLiveData<List<ScrapModel>>()
    val gourResult: LiveData<List<ScrapModel>> get() = _gourResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun gourmetServerResults(q: String) = viewModelScope.launch {
        kotlin.runCatching {
            // loading start
            _isLoading.value = true

            val result = searchBlog(q)
            val gourmetItems = ArrayList<ScrapModel>()
            result.items?.let {
                for (i in it.indices) {
                    gourmetItems.add(it[i].toScrapModel())
                }
                _gourResult.value = gourmetItems
                // loading end
                _isLoading.value = false

                Log.d("TripMates", "gourmet데이터 : ${gourmetItems}")
                Log.d("TripMates", "gourmet데이터 : ${gourmetItems[0].url}")
                Log.d("TripMates", "gourmet데이터 : ${gourmetItems[0].title}")
                Log.d("TripMates", "gourmet데이터 : ${gourmetItems[0].description}")
                Log.d("TripMates", "gourmet데이터 : ${gourmetItems[0].bloggername}")
                Log.d("TripMates", "gourmet데이터 : ${gourmetItems[0].bloggerlink}")
                Log.d("TripMates", "gourmet데이터 : ${gourmetItems[0].postdate}")
            }
        }.onFailure {
            _isLoading.value = false
            Log.e("ScrapViewModel", it.message.toString())
        }
    }
}