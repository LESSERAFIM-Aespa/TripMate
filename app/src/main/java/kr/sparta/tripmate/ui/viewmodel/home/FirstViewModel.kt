package kr.sparta.tripmate.ui.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.data.repository.home.HomeFirstRepositoryImpl
import kr.sparta.tripmate.data.model.scrap.ScrapModel

class FirstViewModel() : ViewModel() {
    private val homeRepository = HomeFirstRepositoryImpl()

    fun getFirstData(uid:String): LiveData<MutableList<ScrapModel>> {
        return homeRepository.getData(uid)
    }
}