package kr.sparta.tripmate.ui.scrap

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.ui.viewmodel.scrap.ScrapViewModel
import kr.sparta.tripmate.util.method.shortToast

class SearchScrollListener(
    private val scrapViewModel: ScrapViewModel, private val scrapFragment:
    ScrapFragment, private val context: Context
) : RecyclerView
.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (ScrapViewModel.currentDisplay in 0..100) {
            if (scrapFragment.searchLoading && !recyclerView.canScrollVertically(1)) {
                if (!scrapFragment.searchQuery.isNullOrEmpty()) {
                    Log.d("TripMates", "currentDisplay : ${ScrapViewModel.currentDisplay}")
                    ScrapViewModel.currentDisplay += 10
                    scrapViewModel.searchAPIResult(scrapFragment.searchQuery!!, context)
                }
            }
        } else context.shortToast("한 번에 표시할 검색 결과 개수는 100개입니다.")
    }
}