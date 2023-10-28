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

class HomeBoardListAdapter(private val onItemClick: (CommunityEntity, Int) -> Unit) :
    ListAdapter<CommunityEntity, HomeBoardListAdapter.Holder>(object : DiffUtil
    .ItemCallback<CommunityEntity>() {
        override fun areItemsTheSame(
            oldItem: CommunityEntity,
            newItem: CommunityEntity
        ): Boolean {
            return oldItem.userid == newItem.userid
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
            }else homeGridImage.setImageResource(R.drawable.emptycommu)
            itemView.setOnClickListener {
                onItemClick(item, bindingAdapterPosition)
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