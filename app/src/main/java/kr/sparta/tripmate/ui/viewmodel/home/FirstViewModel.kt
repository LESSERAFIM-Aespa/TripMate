package kr.sparta.tripmate.ui.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.data.repository.home.HomeFirstRepositorylmpl
import kr.sparta.tripmate.domain.model.ScrapModel

class FirstViewModel() : ViewModel() {
    private val homeRepository = HomeFirstRepositorylmpl()

    fun getFirstData(uid:String): LiveData<MutableList<ScrapModel>> {
        return homeRepository.getData(uid)
    }
}