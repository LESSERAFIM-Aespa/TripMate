package kr.sparta.tripmate.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.ui.mypage.board.BoardModel

class MainSharedViewModel : ViewModel() {
    private val _homeEvent : MutableLiveData<MainSharedEventForHome> = MutableLiveData()
    val homeEvent get() = _homeEvent

    private val _myPageEvent : MutableLiveData<MainSharedEventForMyPage> = MutableLiveData()
    val myPageEvent  get() = _myPageEvent

    fun updateScrapItem(item: ScrapModel?) {
        item?.let{
            // 내정보 페이지의 북마크 업데이트
            _myPageEvent.value = MainSharedEventForMyPage.UpdateScrapItem(it)
            // 홈의 스크랩 업데이트
            _homeEvent.value = MainSharedEventForHome.UpdateScrapItem(it)
        }
    }

    sealed interface MainSharedEventForHome {
        data class UpdateScrapItem(
            val item: ScrapModel
        ): MainSharedEventForHome

        data class UpdateBoardItem(
            val item: BoardModel
        ): MainSharedEventForHome
    }

    sealed interface MainSharedEventForMyPage {
        data class UpdateScrapItem(
            val item: ScrapModel
        ): MainSharedEventForMyPage

        data class UpdateBoardItem(
            val item: BoardModel
        ): MainSharedEventForMyPage
    }
}