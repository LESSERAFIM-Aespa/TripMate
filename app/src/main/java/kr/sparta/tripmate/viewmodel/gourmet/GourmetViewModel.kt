package kr.sparta.tripmate.viewmodel.gourmet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.api.NetWorkInterface
import kr.sparta.tripmate.api.model.GourmetModel
import kr.sparta.tripmate.api.serverdata.naver.GourmetItem
import kr.sparta.tripmate.api.serverdata.naver.GourmetServerData
import kr.sparta.tripmate.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GourmetViewModel(private val apiService:NetWorkInterface): ViewModel() {
    private val _gourResult = MutableLiveData<List<GourmetModel>>()
    val gourResult : LiveData<List<GourmetModel>> get() = _gourResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val gourmetItems : ArrayList<GourmetModel> = ArrayList()
    fun GourmetServerResults(q:String){
        _isLoading.value = true
        gourmetItems.clear()
        val gourmetKey = hashMapOf("query" to q, "sort" to "comment", "display" to "5")

        apiService.getGourmet(gourmetKey)?.enqueue(object: Callback<GourmetServerData?>{
            override fun onResponse(
                call: Call<GourmetServerData?>,
                response: Response<GourmetServerData?>
            ) {
                response.body()?.let {
                    Log.d("GourmetItem","gourmet데이터 : ${it}")
                    for(items in response.body()!!.items){
                        val title = items.title
                        val url = items.link
                        gourmetItems.add(GourmetModel(title,url))
                    }
                    Log.d("GourmetItem","gourmet데이터 : ${gourmetItems}")
                    Log.d("GourmetItem","gourmet데이터 : ${gourmetItems[0].url}")
                    Log.d("GourmetItem","gourmet데이터 : ${gourmetItems[0].title}")
                }
                GourmetDataResults()
            }

            override fun onFailure(call: Call<GourmetServerData?>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun GourmetDataResults(){
        _gourResult.value = gourmetItems
        _isLoading.value = false
    }
}