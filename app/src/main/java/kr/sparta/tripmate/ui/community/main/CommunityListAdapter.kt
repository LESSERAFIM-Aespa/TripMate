package kr.sparta.tripmate.ui.community.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.model.community.CommunityModel
import kr.sparta.tripmate.databinding.FragmentCommunityMainItemBinding
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.ui.community.CommunityDetailActivity

class CommunityListAdapter(
    private val onProfileClicked: (CommunityModelEntity, Int) -> Unit,
    private val onLikeClicked: (CommunityModelEntity, Int) -> Unit,
    private val onThumbnailClicked: (CommunityModelEntity, Int) -> Unit,
    private val onItemLongClicked: (CommunityModelEntity, Int) -> Unit
) :
    ListAdapter<CommunityModelEntity, CommunityListAdapter.CommunityHolder>(
        object : DiffUtil.ItemCallback<CommunityModelEntity>() {
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
                return oldItem == newItem
            }

        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityHolder {
        val view = FragmentCommunityMainItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommunityHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommunityHolder(private val binding: FragmentCommunityMainItemBinding) :
        RecyclerView
        .ViewHolder(binding.root) {
        fun bind(item: CommunityModelEntity) = with(binding) {
            communityMainTitle.text = item.title
            communityMainProfileNickname.text = item.profileNickname
            communityMainThumbnail.setOnClickListener {
                    onProfileClicked(item, bindingAdapterPosition)
                }
            if (!item.addedImage.isNullOrEmpty()){
                communityMainThumbnail.load(item.addedImage)
            } else{communityMainThumbnail.setImageResource(R.drawable.emptycommu)}
            communityMainProfileThumbnail.apply {
                load(item.profileThumbnail)
                setOnClickListener {
                    onThumbnailClicked(item, bindingAdapterPosition)
                }
            }
            communityMainViews.text = item.views
            communityMainLikes.text = item.likes
            communityMainLikesButton.setOnClickListener {
                onLikeClicked(item, bindingAdapterPosition)
            }
            if (item.commuIsLike) {
                communityMainLikesButton.setBackgroundResource(R.drawable.paintedheart)
            } else {
                communityMainLikesButton.setBackgroundResource(R.drawable.heart)
            }
            itemView.setOnLongClickListener {
                onItemLongClicked(item, bindingAdapterPosition)
                true
            }
        }
    }
}