package kr.sparta.tripmate.ui.community.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.databinding.FragmentCommunityMainItemBinding

class CommunityListAdapter(private val dataModelList: MutableList<CommunityModel>): ListAdapter<CommunityModel, CommunityListAdapter.CommunityHolder>(
    object: DiffUtil.ItemCallback<CommunityModel>() {
        override fun areItemsTheSame(oldItem: CommunityModel, newItem: CommunityModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CommunityModel, newItem: CommunityModel): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityHolder {
        val view = FragmentCommunityMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommunityHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CommunityHolder(private val binding : FragmentCommunityMainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommunityModel)=with(binding) {
            communityMainThumbnail.load(item.thumbnail)
            communityMainTitle.text = item.title
            communityMainProfileNickname.text = item.profileNickname
            communityMainProfileThumbnail.load(item.profileThumbnail)
            communityMainViews.text = item.views
            communityMainLikes.text = item.likes
        }
    }
}