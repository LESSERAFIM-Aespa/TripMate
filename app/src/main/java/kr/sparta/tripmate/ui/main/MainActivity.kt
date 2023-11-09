package kr.sparta.tripmate.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy { ViewPager2Adapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewPager2State()
        setupTabIcons()

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2
        ) { tab, position ->
            tab.setText(viewPagerAdapter.getTitme(position))
            tab.setIcon(viewPagerAdapter.getIcon(position))
        }.attach()

        pageChangeCallBack()
        binding.viewPager2.offscreenPageLimit = viewPagerAdapter.itemCount
    }

    private fun viewPager2State() {
        binding.viewPager2.apply {
            adapter = viewPagerAdapter
            setCurrentItem(
                viewPagerAdapter.findFragmentTabIndex(R.string.main_tab_title_home),
                false
            )
            setUserInputEnabled(false)
        }
    }

    private fun pageChangeCallBack() {
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var currentState = 0
            var currentPosition = 0

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPosition == position) {
                    if (currentPosition == 0) binding.viewPager2.currentItem = 4
                    else if (currentPosition == 4) binding.viewPager2.currentItem = 0
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                currentState = state
                super.onPageScrollStateChanged(state)
            }
        })
    }

    /**
     * 작성자: 서정한
     * 내용: 홈 탭에서 다른 탭으로 이동시 사용
     * ViewPagerAdapter에서 Tab 이름을 검색하여 나온
     * index값을 가져와 Tab 현재위치를 바꿔줌
     * */
    fun moveTabFragment(title: Int) {
        val index = viewPagerAdapter.findFragmentTabIndex(title)
        binding.viewPager2.setCurrentItem(index, false)
    }
    /**
     * 작성자: 윤동현
     * 내용: 탭 레이아웃에 클릭이벤트 처리.
     * 클릭된 텝레이아웃의 position을 받아와 클릭됬을때 이미지를 교체해줌.
     * */
    private fun setupTabIcons() {
        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val icon = when (tab?.position){
                    0 -> R.drawable.budget2
                    1 -> R.drawable.community2
                    2 -> R.drawable.home2
                    3 -> R.drawable.scrap2
                    4 -> R.drawable.mypage2
                    else -> R.drawable.budget
                }
                tab?.setIcon(icon)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val icon = when (tab?.position){
                    0 -> R.drawable.budget
                    1 -> R.drawable.community
                    2 -> R.drawable.home
                    3 -> R.drawable.scrap
                    4 -> R.drawable.mypage
                    else -> R.drawable.budget // 기본이미지 대체는 어떻게 해야 좋을까 음..
                }
                tab?.setIcon(icon)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //딱히 처리할게 없음
            }
        })
    }
}