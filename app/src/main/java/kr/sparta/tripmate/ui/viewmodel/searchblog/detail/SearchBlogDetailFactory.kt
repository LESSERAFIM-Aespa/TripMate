package kr.sparta.tripmate.ui.viewmodel.searchblog.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.sparta.tripmate.api.naver.NaverNetWorkClient
import kr.sparta.tripmate.data.datasource.remote.community.FirebaseBookmarkRemoteDataSource
import kr.sparta.tripmate.data.repository.SearchBlogRepositoryImpl
import kr.sparta.tripmate.data.repository.community.FirebaseBookmarkRepositoryImpl
import kr.sparta.tripmate.domain.repository.community.FirebaseBookmarkRepository
import kr.sparta.tripmate.domain.repository.search.SearchBlogRepository
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.AddBookmarkedBlogUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.GetAllBookmarkedBlogsUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.RemoveBookmarkedBlogUseCase
import kr.sparta.tripmate.domain.usecase.community.bookmark.blog.UpdateBookmarkedBlogUseCase
import kr.sparta.tripmate.domain.usecase.search.GetSearchBlogUseCase

class SearchBlogDetailFactory : ViewModelProvider.Factory {

    private val bookmarkRepository: FirebaseBookmarkRepository by lazy {
        FirebaseBookmarkRepositoryImpl(FirebaseBookmarkRemoteDataSource())
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBlogDetailViewModel::class.java)) {
            return SearchBlogDetailViewModel(
                UpdateBookmarkedBlogUseCase(bookmarkRepository)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}