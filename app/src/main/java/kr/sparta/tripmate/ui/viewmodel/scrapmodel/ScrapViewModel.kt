package kr.sparta.tripmate.ui.viewmodel.scrapmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.data.model.ScrapModel
import kr.sparta.tripmate.api.naver.NetWorkInterface
import kr.sparta.tripmate.data.model.ScrapServerData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapViewModel(private val apiService: NetWorkInterface): ViewModel() {
    private val _gourResult = MutableLiveData<List<ScrapModel>>()
    val gourResult : LiveData<List<ScrapModel>> get() = _gourResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val gourmetItems : ArrayList<ScrapModel> = ArrayList()
    fun GourmetServerResults(q:String){
        _isLoading.value = true
        gourmetItems.clear()
        val gourmetKey = hashMapOf("query" to q, "sort" to "sim", "display" to "10")

        apiService.getScrap(gourmetKey)?.enqueue(object: Callback<ScrapServerData?>{
            override fun onResponse(
                call: Call<ScrapServerData?>,
                response: Response<ScrapServerData?>
            ) {
                response.body()?.let {
                    Log.d("GourmetItem","gourmet데이터 : ${it}")
                    for(items in response.body()!!.items){
                        val title = items.title ?:""
                        val url = items.link ?:""
                        val description = items.description ?:""
                        val bloggername = items.bloggername ?:""
                        val bloggerlink = items.bloggerlink ?:""
                        val postdate = items.postdate ?:""
                        gourmetItems.add(ScrapModel(title, url, description, bloggername,
                            bloggerlink, postdate))
                    }
                    Log.d("TripMates","gourmet데이터 : ${gourmetItems}")
                    Log.d("TripMates","gourmet데이터 : ${gourmetItems[0].url}")
                    Log.d("TripMates","gourmet데이터 : ${gourmetItems[0].title}")
                    Log.d("TripMates","gourmet데이터 : ${gourmetItems[0].description}")
                    Log.d("TripMates","gourmet데이터 : ${gourmetItems[0].bloggername}")
                    Log.d("TripMates","gourmet데이터 : ${gourmetItems[0].bloggerlink}")
                    Log.d("TripMates","gourmet데이터 : ${gourmetItems[0].postdate}")
                }
                GourmetDataResults()
            }

            override fun onFailure(call: Call<ScrapServerData?>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun GourmetDataResults(){
        _gourResult.value = gourmetItems
        _isLoading.value = false
    }
}