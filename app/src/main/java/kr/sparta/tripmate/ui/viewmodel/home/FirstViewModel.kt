package kr.sparta.tripmate.ui.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.data.repository.home.HomeFirstRepositoryImpl
import kr.sparta.tripmate.data.model.scrap.ScrapModel
<<<<<<< HEAD
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
=======
>>>>>>> c8d8871 ([hotfix] 에러 수정 및 리펠토링)

class FirstViewModel() : ViewModel() {
    private val homeRepository = HomeFirstRepositoryImpl()

<<<<<<< HEAD
    fun getFirstData(uid:String): LiveData<MutableList<ScrapEntity>> {
=======
    fun getFirstData(uid:String): LiveData<MutableList<ScrapModel>> {
>>>>>>> c8d8871 ([hotfix] 에러 수정 및 리펠토링)
        return homeRepository.getData(uid)
    }
}