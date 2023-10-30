package kr.sparta.tripmate.ui.viewmodel.scrap

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.RemoveFirebaseScrapData

class ScrapDetailViewModel(
    private val removeFirebaseScrapData: RemoveFirebaseScrapData
) : ViewModel() {
    private val _scrapDetailResults: MutableLiveData<List<ScrapEntity?>> = MutableLiveData()
    val scrapDetailResults get() = _scrapDetailResults
    fun removeFirebaseScrapData(uid: String, model: ScrapEntity) =
        removeFirebaseScrapData.invoke(uid, model)
    fun updateIsLike(model: ScrapEntity) {
        val list = scrapDetailResults.value.orEmpty().toMutableList()
        list.find { it?.url == model.url } ?: return

        _scrapDetailResults.value = list
    }
}