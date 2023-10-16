package kr.sparta.tripmate.ui.mypage.scrap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.sparta.tripmate.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {
    companion object {
        fun newInstance(): BookmarkFragment = BookmarkFragment()
    }

    private var _binding : FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy {
        BookmarkListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView()=with(binding) {
        bookmarkRecyclerview.adapter = adapter
        bookmarkRecyclerview.setHasFixedSize(true)
    }
}