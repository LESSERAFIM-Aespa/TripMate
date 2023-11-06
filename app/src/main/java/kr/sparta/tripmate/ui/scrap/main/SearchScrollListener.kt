package kr.sparta.tripmate.ui.scrap.main

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.ui.viewmodel.scrap.main.SearchBlogViewModel
import kr.sparta.tripmate.util.method.shortToast

class SearchScrollListener(
    private val scrapViewModel: SearchBlogViewModel, private val scrapFragment:
    ScrapFragment, private val context: Context
) : RecyclerView
.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (SearchBlogViewModel.currentDisplay in 0..100) {
            if (scrapFragment.searchLoading && !recyclerView.canScrollVertically(1)) {
                if (!scrapFragment.searchQuery.isNullOrEmpty()) {
                    Log.d("TripMates", "currentDisplay : ${SearchBlogViewModel.currentDisplay}")
                    SearchBlogViewModel.currentDisplay += 10
                    scrapViewModel.searchAPIResult(scrapFragment.searchQuery!!, context)
                }
            }
        } else context.shortToast("한 번에 표시할 검색 결과 개수는 100개입니다.")
    }
}