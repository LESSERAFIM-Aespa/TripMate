package kr.sparta.tripmate.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.databinding.HomeFirstItemsBinding
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.util.method.removeHtmlTags

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
    inner class Holder(private val binding: HomeFirstItemsBinding) : RecyclerView
    .ViewHolder(binding.root) {
        fun bind(item: CommunityModelEntity) = with(binding) {

            homeFirstTitle.text = item.title
            homeFirstImage.load(item.addedImage)

            itemView.setOnClickListener {
                onItemClick(item, bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = HomeFirstItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(getItem(position))
    }


}