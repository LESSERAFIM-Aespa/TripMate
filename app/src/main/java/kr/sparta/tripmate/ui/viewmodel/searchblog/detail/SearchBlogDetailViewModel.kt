package kr.sparta.tripmate.ui.viewmodel.searchblog.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.GetAllBookmarkedBlogsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.RemoveBookmarkedBlogUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.UpdateBookmarkedBlogUseCase

class SearchBlogDetailViewModel(
    private val updateBookmarkedBlogUseCase: UpdateBookmarkedBlogUseCase,
) : ViewModel() {
//
//    private val _scrapDetailResults: MutableLiveData<List<SearchBlogEntity?>> = MutableLiveData()
//    val scrapDetailResults get() = _scrapDetailResults

    /**
     * 작성자: 서정한
     * 내용: 블로그 북마크여부 업데이트
     * */
    suspend fun updateBookmarkedBlog(uid: String, model: SearchBlogEntity) = viewModelScope.launch {
        updateBookmarkedBlogUseCase.invoke(uid, model)
    }

}