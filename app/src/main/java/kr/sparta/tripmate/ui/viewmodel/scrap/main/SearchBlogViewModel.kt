package kr.sparta.tripmate.ui.viewmodel.scrap.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.scrap.ImageItemsEntity
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.model.search.toEntity
import kr.sparta.tripmate.domain.usecase.GetImageUseCase
import kr.sparta.tripmate.domain.usecase.GetSearchBlogUseCase
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetAllBlogScrapsUseCase
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.UpdateBlogScrapUseCase
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class SearchBlogViewModel(
    private val searchBlog: GetSearchBlogUseCase,
    private val updateBlogScrapUseCase: UpdateBlogScrapUseCase,
    private val getAllBlogScraps: GetAllBlogScrapsUseCase,
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {
    private val _searchList = MutableLiveData<List<SearchBlogEntity>>()
    val searchList: LiveData<List<SearchBlogEntity>> get() = _searchList

    private val _isLoading = MutableLiveData<Boolean>()

    private val _recommandImage = MutableLiveData<List<ImageItemsEntity>>()
    val recommandImage :LiveData<List<ImageItemsEntity>> get() = _recommandImage

    val isLoading: LiveData<Boolean> get() = _isLoading

    companion object {
        var currentDisplay = 10
    }

    /**
     * 작성자: 박성수
     * 내용: 네이버 API로 블로그 검색한 결과를 받아옴
     * */
    fun searchAPIResult(q: String, uid: String) = viewModelScope.launch {
        /**
         * 작성자: 서정한
         * 내용: 검색결과에 내가 스크랩한 블로그 표시
         * */
        suspend fun applyBlogScraps(scrapItems: ArrayList<SearchBlogEntity>) {
            getAllBlogScraps(uid).collect() { blogs ->
                val scraps = blogs.toMutableList().toEntity()

                for (i in 0 until scrapItems.size) {
                    val item = scrapItems[i]
                    val isExist = scraps.any { it.link == item.link }
                    if (isExist) {
                        scrapItems[i] = item.copy(
                            isLike = true
                        )
                    }else {
                        scrapItems[i] = item.copy(
                            isLike = false
                        )
                    }
                }
                _searchList.value = scrapItems

                // loading end
                _isLoading.value = false
            }
        }

        // loading start
        _isLoading.value = true

        val result = searchBlog(q, currentDisplay)
        val scrapItems = ArrayList<SearchBlogEntity>()
        result.items?.let { searchList ->
            for (i in searchList.indices) {
                scrapItems.add(searchList[i])
            }

            // 블로그 검색결과에 내가 스크랩한 블로그 표시
            applyBlogScraps(scrapItems)
        }
    }

    fun searchImageResult(q: String) = viewModelScope.launch {
        kotlin.runCatching {
            _isLoading.value = true
            val result = getImageUseCase(q)
            val imageItems = ArrayList<ImageItemsEntity>()
            result.items?.let {
                for(i in it.indices){
                    imageItems.add(it[i])
                }
                _recommandImage.value = imageItems
                _isLoading.value = false
            }
        }.onFailure {
            _isLoading.value = false
            Log.e("tripmates", it.message.toString())
        }
    }


    /**
     * 작성자: 서정한
     * 내용: 블로그의 스크랩목록을 업데이트합니다.
     * 스크랩할 블로그 model이 리스트에 있을경우 삭제합니다.
     * 리스트에 없을경우 추가합니다.
     * */
    fun updateBlogScrap(uid: String, model: SearchBlogEntity) = updateBlogScrapUseCase(uid, model)

    /**
     * 작성자: 서정한
     * 내용: 게시글에 좋아요를 누른 유저목록을 업데이트합니다.
     * */
    fun updateIsLike(model: SearchBlogEntity, position: Int) {
        val list = _searchList.value.orEmpty().toMutableList()
        list.find { it.link == model.link } ?: return

        list[position] = model
        _searchList.value = list
    }

    /**
     * 작성자: 서정한
     * 내용: 검색내역 Clear
     * */
    fun clearSearchList() {
        _searchList.value = listOf()
    }
}