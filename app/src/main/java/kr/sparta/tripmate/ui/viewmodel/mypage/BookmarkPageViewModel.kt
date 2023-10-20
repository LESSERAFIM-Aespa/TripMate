package kr.sparta.tripmate.ui.viewmodel.mypage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.usecase.GetFirebaseScrapData
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class BookmarkPageViewModel(private val getFirebaseScrapData: GetFirebaseScrapData): ViewModel() {
    // 내정보 스크랩 LiveData
    private val _mypageScraps : MutableLiveData<List<ScrapEntity?>> = MutableLiveData()
    val myPageList get() = _mypageScraps
//    private val _mypagePosts : MutableLiveData<List<BoardModel>> = MutableLiveData()
//    val mypagePosts get() = _mypagePosts

    // Firebase RDB에서 Scrap데이터를 가져온 결과를 livedata에 업데이트 해준다.
    fun updateScrapData(context: Context) {
        val uid = SharedPreferences.getUid(context)
        getFirebaseScrapData.invoke(uid, _mypageScraps)
    }
}