package kr.sparta.tripmate.ui.community.main

import android.content.Intent
import android.os.Bundle
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


class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommunityViewModel by viewModels()

    private val adapter by lazy {
        CommunityListAdapter(viewModel.dataModelList.value ?: mutableListOf())
    }

    // Write a message to the database
    private val database = Firebase.database
    private val communityRef = database.getReference("CommunityData")



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
        val myRef = database.getReference("CommunityData").child(Firebase.auth.currentUser!!.uid)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newData = mutableListOf<CommunityModel>()
                for (dataModel in snapshot.children) {
                    newData.add(dataModel.getValue(CommunityModel::class.java)!!)
                }

                // 가져온 데이터를 ViewModel을 통해 업데이트
                viewModel.updateDataModelList(newData)

                // RecyclerView 어댑터 업데이트
                adapter.submitList(newData)
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
