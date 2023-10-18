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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.sparta.tripmate.databinding.FragmentCommunityBinding
import kr.sparta.tripmate.ui.community.CommunityWriteActivity
import kr.sparta.tripmate.ui.viewmodel.community.CommunityViewModel
import kotlin.math.log


class CommunityFragment : Fragment() {

    private val allPostIds = mutableListOf<String>()
    private val allCommunityData = mutableListOf<CommunityModel>()

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommunityViewModel by viewModels()

    private val adapter by lazy {
        CommunityListAdapter(viewModel.dataModelList.value ?: mutableListOf())
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

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, CommunityWriteActivity::class.java)
            startActivity(intent)
        }

        // Firebase에서 데이터를 가져오고 RecyclerView 어댑터를 업데이트
        val database = Firebase.database
        val myRef = database.getReference("CommunityData")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allCommunityData = mutableListOf<CommunityModel>()

                for (userPostSnapshot in snapshot.children) {
                    for (postSnapshot in userPostSnapshot.children) {
                        val postId = postSnapshot.key
                        val postModel = postSnapshot.getValue(CommunityModel::class.java)
                        if (postId != null && postModel != null) {
                            allCommunityData.add(postModel)
                        }
                    }
                }


                // 가져온 데이터를 ViewModel을 통해 업데이트
                viewModel.updateDataModelList(allCommunityData)
                // RecyclerView 어댑터 업데이트
                adapter.submitList(allCommunityData)

            }

            override fun onCancelled(error: DatabaseError) {
                // 오류 처리
            }
        })


        initview()
        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, CommunityWriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initview() = with(binding) {
        communityMainRecyclerView.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
