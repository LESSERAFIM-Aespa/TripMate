package kr.sparta.tripmate.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.HomeGridviewItemsBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity

class HomeBoardListAdapter(private val onItemClick: (CommunityEntity) -> Unit) :
    ListAdapter<CommunityEntity, HomeBoardListAdapter.Holder>(object : DiffUtil
    .ItemCallback<CommunityEntity>() {
        override fun areItemsTheSame(
            oldItem: CommunityEntity,
            newItem: CommunityEntity
        ): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(
            oldItem: CommunityEntity,
            newItem: CommunityEntity
        ): Boolean {
            return oldItem == newItem
        }

    }) {
    inner class Holder(private val binding: HomeGridviewItemsBinding) : RecyclerView
    .ViewHolder(binding.root) {
        fun bind(item: CommunityEntity) = with(binding) {
            homeGridTitle.text = item.title
            if(!item.image.isNullOrEmpty()){
                homeGridImage.load(item.image){
                    memoryCacheKey(item.image)
                    crossfade(true)
                    listener(
                        onStart = {
                            // 로딩시작
                            homeGridImageProgressbar.visibility = View.VISIBLE
                        },
                        onSuccess = { request, result ->
                            // 로딩종료
                            homeGridImageProgressbar.visibility = View.GONE
                        }
                    )
                }
            } else {
                homeGridImage.load(R.drawable.emptycommu)
            }

            // 게시글 클릭시 이동
            itemView.setOnClickListener {
                onItemClick(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = HomeGridviewItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(getItem(position))
    }


}