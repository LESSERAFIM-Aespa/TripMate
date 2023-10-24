package kr.sparta.tripmate.ui.userprofile.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.sparta.tripmate.databinding.FragmentUserProfileBoardBinding

/**
 * 작성자: 서정한
 * 내용: 해당User가 작성한 글목록을 보여주는 Fragment
 * */
class UserProfileBoardFragment : Fragment() {
    companion object{
        fun newInstance() : UserProfileBoardFragment = UserProfileBoardFragment()
    }

    private var _binding : FragmentUserProfileBoardBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        UserProfileBoardListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView()=with(binding) {
        userProfileBoardRecyclerview.adapter = adapter
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}