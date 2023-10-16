package kr.sparta.tripmate.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ActivityMainBinding
import kr.sparta.tripmate.util.method.setIcon

class MainActivity : AppCompatActivity(), TabLayoutListener {
    companion object {
        fun newIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val tabList = listOf("Budget", "Commu", "Home", "Scrap", "MyPage")
    private val it = this@MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        ViewPager2State()
        TabLayoutMediator()
        PageChangeCallBack()

    }

    private fun TabLayoutMediator() {
        com.google.android.material.tabs.TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2
        ) { tab, position ->
            tab.text = tabList[position]
            when (position) {
                0 -> tab.setIcon(it, R.drawable.budget)
                1 -> tab.setIcon(it, R.drawable.community)
                2 -> tab.setIcon(it, R.drawable.home)
                3 -> tab.setIcon(it, R.drawable.scrap)
                4 -> tab.setIcon(it, R.drawable.mypage)
            }
        }.attach()
    }

    private fun ViewPager2State() {
        binding.viewPager2.apply {
            adapter = ViewPager2Adapter(it)
            setCurrentItem(2, false)
            setUserInputEnabled(false)
        }
    }

    private fun PageChangeCallBack() {
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

    override fun onScrapClicked() {
        clickedViewPager2(3)
    }

    override fun onMyPageClicked() {
        clickedViewPager2(4)
    }

    override fun onCommuClicked() {
        clickedViewPager2(1)
    }

    override fun onBudgetClicked() {
        clickedViewPager2(0)
    }

    private fun clickedViewPager2(item: Int) {
        binding.viewPager2.setCurrentItem(item, true)
    }
}