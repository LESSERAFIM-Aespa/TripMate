package kr.sparta.tripmate.ui.viewmodel.mypage

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.firebase.BoardKeyModelEntity
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.domain.model.firebase.toCommunity
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.GetFirebaseBoardDataFromBoardRepo
import kr.sparta.tripmate.domain.usecase.firebaseboardrepository.SaveBoardFirebase
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
    private val getFirebaseBoardKeyDataFromScrapRepo: GetFirebaseBoardKeyDataFromScrapRepo,
    private val saveBoardFirebase: SaveBoardFirebase
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

    fun updateBoardData(uid: String) {
        getFirebaseBoardDataFromScrapRepo.invoke(uid, _mypageBoard)
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
            if (selectItem != null) {
                selectItem.boardIsLike = boardKeyItem!!.myBoardIsLike
            }
        }

        val bookMarkBoardData = boardData.filter { it?.boardIsLike == true }

        _totalMyPage.value = scraps + bookMarkBoardData
    }

    fun saveBoardFirebase(model: CommunityModelEntity) {
        val currentView = model.views?.toIntOrNull() ?: 0
        val newViews = currentView + 1
        Log.d("ssss", "조회수오르니? ${newViews}")
        model.views = newViews.toString()
        saveBoardFirebase(model, _mypageBoard)
        updateBoardData(model.id)
    }
}