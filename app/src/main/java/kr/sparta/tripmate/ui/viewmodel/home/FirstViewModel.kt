package kr.sparta.tripmate.ui.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.data.repository.home.HomeFirstRepositoryImpl
import kr.sparta.tripmate.data.model.scrap.ScrapModel
<<<<<<< HEAD
<<<<<<< HEAD
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
=======
>>>>>>> c8d8871 ([hotfix] 에러 수정 및 리펠토링)
=======
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
>>>>>>> a4d949b ([Refector] : ScrapModel -> ScrapEntity 변경)

class FirstViewModel() : ViewModel() {
    private val homeRepository = HomeFirstRepositoryImpl()

<<<<<<< HEAD
<<<<<<< HEAD
    fun getFirstData(uid:String): LiveData<MutableList<ScrapEntity>> {
=======
    fun getFirstData(uid:String): LiveData<MutableList<ScrapModel>> {
>>>>>>> c8d8871 ([hotfix] 에러 수정 및 리펠토링)
=======
    fun getFirstData(uid:String): LiveData<MutableList<ScrapEntity>> {
>>>>>>> a4d949b ([Refector] : ScrapModel -> ScrapEntity 변경)
        return homeRepository.getData(uid)
    }
}