package kr.sparta.tripmate.ui.viewmodel.scrap.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.UpdateBlogScrapUseCase

class ScrapDetailViewModel(
    private val updateBlogScrapUseCase: UpdateBlogScrapUseCase,
) : ViewModel() {
    private val _isScrap: MutableLiveData<Boolean> = MutableLiveData()
    val isScrap get() = _isScrap

    /**
     * 작성자: 서정한
     * 내용: 블로그의 스크랩버튼 클릭시 상태 업데이트
     * */
    fun updateIsScraped(isScraped: Boolean) {
        _isScrap.value = isScraped
    }

    /**
     * 작성자: 서정한
     * 내용: 스크랩한 블로그 목록을 업데이트합니다.
     * 블로그 목록에 없는 item일 경우 추가하고
     * 있는경우 제거합니다.
     * */
    fun updateBlogScrap(uid: String, model: SearchBlogEntity) = updateBlogScrapUseCase(uid, model)
}