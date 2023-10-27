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
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity

class HomeBoardListAdapter(private val onItemClick: (CommunityModelEntity, Int) -> Unit) :
    ListAdapter<CommunityModelEntity, HomeBoardListAdapter.Holder>(object : DiffUtil
    .ItemCallback<CommunityModelEntity>() {
        override fun areItemsTheSame(
            oldItem: CommunityModelEntity,
            newItem: CommunityModelEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CommunityModelEntity,
            newItem: CommunityModelEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }) {
    inner class Holder(private val binding: HomeGridviewItemsBinding) : RecyclerView
    .ViewHolder(binding.root) {
        fun bind(item: CommunityModelEntity) = with(binding) {

            homeGridTitle.text = item.title
            if(!item.addedImage.isNullOrEmpty()){
                homeGridImage.load(item.addedImage){
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