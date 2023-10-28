package kr.sparta.tripmate.ui.viewmodel.searchblog.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.model.search.toSearchBlog
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.AddBookmarkedBlogUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.GetAllBookmarkedBlogsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.RemoveBookmarkedBlogUseCase
import kr.sparta.tripmate.domain.usecase.search.GetSearchBlogUseCase
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class SearchBlogViewModel(
    private val getAllBookmarkedBlogsUseCase: GetAllBookmarkedBlogsUseCase,
    private val searchBlogUseCase: GetSearchBlogUseCase,
    private val addBookmarkBlogsUseCase: AddBookmarkedBlogUseCase,
    private val removeBookmarkedBlogUseCase: RemoveBookmarkedBlogUseCase,
) : ViewModel() {
    private val _searchBlogs = MutableLiveData<List<SearchBlogEntity>>()
    val searchBlogs: LiveData<List<SearchBlogEntity>> get() = _searchBlogs

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /**
     * 작성자: 박성수
     * 내용: 네이버 API로 블로그 검색한 결과를 받아옴
     * */
    suspend fun searchAPIResult(q: String, context: Context) = viewModelScope.launch {
        kotlin.runCatching {
            // loading start
            _isLoading.value = true

            // 네이버 검색결과응답
            val searchResponse = searchBlogUseCase.invoke(q, 10)

            // 블로그 검색결과
            val searchedBlogList = searchResponse.items?.toSearchBlog()?.toMutableList()

            val uid = SharedPreferences.getUid(context)
            // 전체 북마크된 블로그 목록
            val bookmarkedBlogList = getAllBookmarkedBlogsUseCase.invoke(uid)

            // 블로그 검색결과 중 북마크 된 목록만 화면에 북마크표시를 해줍니다.
            searchedBlogList?.let {
                bookmarkedBlogList.forEachIndexed { index, bookmarkedEntity ->
                    it.forEachIndexed { idx, searchedEntity ->
                        if(bookmarkedEntity.url == searchedEntity.url) {
                            searchedBlogList[index] = searchedEntity
                        }
                    }
                }
            }

            _searchBlogs.value = searchedBlogList?.toList()

            // loading end
            _isLoading.value = false

        }.onFailure {
            _isLoading.value = false
            Log.e("ScrapViewModel", it.message.toString())
        }
    }

    /**
     * 작성자: 서정한
     * 내용: 선택한 스크랩 item을 Firebase RDB에 저장합니다.
     * */
    suspend fun saveScrap(uid: String, model: SearchBlogEntity) = viewModelScope.launch {
        addBookmarkBlogsUseCase.invoke(uid, model)
    }

    /**
     * 작성자: 서정한
     * 내용: Firebase RDB에 저장된 아이템에서 선택한 스크랩 item을 찾아 삭제합니다.
     * */
    suspend fun removeScrap(uid: String, model: SearchBlogEntity) = viewModelScope.launch {
        removeBookmarkedBlogUseCase.invoke(uid, model.url)
    }

    /**
     * 작성자: 서정한
     * 내용: 블로그 item의 좋아요상태를 업데이트합니다.
     * */
    suspend fun updateIsLike(model: SearchBlogEntity) {
        val list = searchBlogs.value.orEmpty().toMutableList()
        list.forEachIndexed { index, searchBlogEntity ->
            if(list[index].url == model.url) {
               list[index] = model
            }
        }

        _searchBlogs.value = list
    }
}