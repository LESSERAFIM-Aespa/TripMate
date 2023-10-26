package kr.sparta.tripmate.ui.viewmodel.mypage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetFirebaseBoardDataFromScrapRepo
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetFirebaseBoardKeyDataFromScrapRepo
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetFirebaseScrapData
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.UpdateCommuIsViewFromScrapRepo
import kr.sparta.tripmate.util.ScrapInterface
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class BookmarkPageViewModel(
    private val getFirebaseScrapData: GetFirebaseScrapData,
    private val getFirebaseBoardDataFromScrapRepo: GetFirebaseBoardDataFromScrapRepo,
    private val updateCommuIsViewFromScrapRepo: UpdateCommuIsViewFromScrapRepo,
    private val getFirebaseBoardKeyDataFromScrapRepo: GetFirebaseBoardKeyDataFromScrapRepo
) : ViewModel() {

    private val _mypageScraps: MutableLiveData<List<ScrapEntity?>> = MutableLiveData()
    val myPageList get() = _mypageScraps
    private val _mypageBoard: MutableLiveData<List<CommunityModelEntity?>> = MutableLiveData()
    val mypageBoard get() = _mypageBoard

    private val _boardKey: MutableLiveData<List<BoardKeyModelEntity?>> = MutableLiveData()
    val boardKey get() = _boardKey

    val _totalMyPage: MutableLiveData<List<ScrapInterface?>> = MutableLiveData()
    val totalMyPage get() = _totalMyPage

    fun updateScrapData(context: Context) = viewModelScope.launch {
        kotlin.runCatching {
            val uid = SharedPreferences.getUid(context)
            getFirebaseScrapData.invoke(uid, _mypageScraps)
        }

    }

    fun updateBoardData(uid: String) = viewModelScope.launch {
        kotlin.runCatching {
            getFirebaseBoardDataFromScrapRepo.invoke(uid, _mypageBoard)
        }
    }

    fun updateBoardDataView(model: CommunityModelEntity, position: Int) {
        updateCommuIsViewFromScrapRepo.invoke(model.toCommunity(), position, _mypageBoard)
    }

    fun getBoardKeyData(uid: String) {
        getFirebaseBoardKeyDataFromScrapRepo(uid, _boardKey)
    }

    fun mergeScrapAndBoardData() {
        val scraps = _mypageScraps.value ?: emptyList()
        val boardData = _mypageBoard.value ?: emptyList()
        val boardKey = _boardKey.value ?: emptyList()
        boardKey.forEach { boardKeyItem ->
            val selectItem = boardData.find { it?.key == boardKeyItem?.key }
            if(selectItem != null){
                selectItem.boardIsLike = boardKeyItem!!.myBoardIsLike
            }
        }

        val bookMarkBoardData = boardData.filter { it?.boardIsLike == true }

        _totalMyPage.value = scraps + bookMarkBoardData
    }
}