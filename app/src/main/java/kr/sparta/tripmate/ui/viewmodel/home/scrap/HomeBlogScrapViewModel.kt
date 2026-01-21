package kr.sparta.tripmate.ui.viewmodel.home.scrap

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.domain.model.search.toEntity
import kr.sparta.tripmate.domain.usecase.firebasescraprepository.GetAllBlogScrapsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 작성자: 서정한
 * 내용: HomeFragment의 블로그 탭 ViewModel
 * */
@HiltViewModel
class HomeBlogScrapViewModel @Inject constructor(private val getAllBlogScraps: GetAllBlogScrapsUseCase) : ViewModel()  {
    private val _blogScraps : MutableLiveData<List<SearchBlogEntity?>> = MutableLiveData()
    val blogScraps get() = _blogScraps

    fun getAllBlogs(uid: String) = CoroutineScope(Dispatchers.Main).launch {
        getAllBlogScraps(uid).collect() {
            _blogScraps.value = it.toEntity()
        }
    }
}