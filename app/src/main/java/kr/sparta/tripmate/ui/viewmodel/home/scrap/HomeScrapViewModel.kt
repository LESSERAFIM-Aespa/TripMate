package kr.sparta.tripmate.ui.viewmodel.home.scrap

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.usecase.GetFirebaseScrapData
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class HomeScrapViewModel(private val getFirebaseScrapData: GetFirebaseScrapData) : ViewModel() {
    private val _homeScraps : MutableLiveData<List<ScrapEntity?>> = MutableLiveData()
    val homeScraps get() = _homeScraps

    fun updateScrapData(context: Context) {
        val uid = SharedPreferences.getUid(context)
        getFirebaseScrapData.invoke(uid, _homeScraps)
    }
}