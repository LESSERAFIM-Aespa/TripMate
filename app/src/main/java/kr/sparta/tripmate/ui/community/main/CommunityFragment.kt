package kr.sparta.tripmate.ui.community.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.databinding.FragmentCommunityBinding
import kr.sparta.tripmate.ui.community.CommunityWriteActivity
import kr.sparta.tripmate.ui.main.MainActivity
import kr.sparta.tripmate.ui.viewmodel.community.CommunityViewModel
import kotlin.math.log

/**
 *
 * 1~8번까지 확인하시고 이상한거 있으면 말씀해주세요
 * 기존의 코드는 주석처리해놨습니다. 확인하시고 말씀해주시면 삭제하겠습니다.
 */
class CommunityFragment : Fragment() {

//    private val allPostIds = mutableListOf<String>()
//    private val allCommunityData = mutableListOf<CommunityModel>()

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommunityViewModel by viewModels()

    private val commuAdapter by lazy {      //1. 클릭 이벤트 구현
        CommunityListAdapter(
            onProfileClicked =
            { model, position ->
                (context as MainActivity).onMyPageClicked()
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commuFloatBtn()     //2. 플로팅버튼
        initView()          //3. 어댑터 관리
        initViewModel()     //4. 뷰모델 관리
//        // Firebase에서 데이터를 가져오고 RecyclerView 어댑터를 업데이트
//        val database = Firebase.database
//        val myRef = database.getReference("CommunityData")
//
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val allCommunityData = mutableListOf<CommunityModel>()
//
//                for (userPostSnapshot in snapshot.children) {
//                    for (postSnapshot in userPostSnapshot.children) {
//                        val postId = postSnapshot.key
//                        val postModel = postSnapshot.getValue(CommunityModel::class.java)
//                        if (postId != null && postModel != null) {
//                            allCommunityData.add(postModel)
//                        }
//                    }
//                }
//
//
//                // 가져온 데이터를 ViewModel을 통해 업데이트
//                viewModel.updateDataModelList(allCommunityData)
//                // RecyclerView 어댑터 업데이트
//                adapter.submitList(allCommunityData)
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // 오류 처리
//            }
//        })


//버튼이 중복됩니다.
//        binding.writeBtn.setOnClickListener {
//            val intent = Intent(context, CommunityWriteActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun initViewModel() {
       viewModel.dataModelList.observe(viewLifecycleOwner){ //5. 뷰모델에서 데이터베이스에서 받아온데이터를 관찰하고 어댑터에 넣어줍니다.
           Log.d("TripMates", "커뮤데이터 :${it[0].views}")
           commuAdapter.submitList(it)
       }
        viewModel.isLoading.observe(viewLifecycleOwner){//6. 뷰모델에서 로딩중인지 감지하고 해당 뷰를
            binding.communityLoading.visibility = if(it) View.VISIBLE else View.GONE
        }
    }

    private fun commuFloatBtn() {
        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, CommunityWriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() = with(binding) {
        communityMainRecyclerView.apply {       //7. 어댑터를 관리합니다.
            adapter = commuAdapter
            communityMainRecyclerView.setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.updateDataModelList()                 //8. 뷰모델의 함수를 호출합니다.

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
