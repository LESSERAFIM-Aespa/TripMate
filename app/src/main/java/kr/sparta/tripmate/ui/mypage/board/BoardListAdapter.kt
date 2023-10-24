package kr.sparta.tripmate.ui.mypage.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentCommunityMainItemBinding
import kr.sparta.tripmate.databinding.FragmentMypageBoardItemBinding
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity

class BoardListAdapter(private val onProfileClicked: (CommunityModelEntity, Int) -> Unit) :
    ListAdapter<CommunityModelEntity, BoardListAdapter.Holder>(object : DiffUtil
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
            return oldItem == newItem
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = FragmentCommunityMainItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class Holder(private val binding: FragmentCommunityMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommunityModelEntity) = with(binding) {
            communityMainTitle.text = item.title
            communityMainProfileNickname.text = item.profileNickname
            communityMainThumbnail.apply {
                load(item.thumbnail)
                setOnClickListener {
                    onProfileClicked(item, bindingAdapterPosition)
                }
            }
            communityMainViews.text = item.views
            communityMainLikes.text = item.likes
            if (item.commuIsLike) {
                communityMainLikesButton.setBackgroundResource(R.drawable.paintedlove)
            } else {
                communityMainLikesButton.setBackgroundResource(R.drawable.love)
            }
        }
    }
}