package kr.sparta.tripmate.ui.userprofile.board

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ItemUserProfileBoardBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.ui.userprofile.model.UserProfileModel

class UserProfileBoardListAdapter(
    private val onItemClicked: (CommunityEntity, Int) -> Unit
) :
    ListAdapter<CommunityEntity, UserProfileBoardListAdapter.UserProfileHolder>(
        object : DiffUtil.ItemCallback<CommunityEntity>() {
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
                return oldItem.key == newItem.key
            }

        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileHolder {
        val view =
            ItemUserProfileBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProfileHolder(view)
    }

    override fun onBindViewHolder(holder: UserProfileHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserProfileHolder(private val binding: ItemUserProfileBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommunityEntity) = with(binding) {
            userboardMainTitle.text = item.title
            userboardMainProfileNickname.text = item.userNickname
            userboardMainThumbnail.apply {
                if (item.image.isNullOrBlank()) {
                    userboardMainThumbnail.setImageResource(R.drawable.emptycommu)
                } else {
                    userboardMainThumbnail.load(item.image) {
                        listener(
                            onStart = {
                                userboardMainImageProgressbar.visibility = View.VISIBLE
                            },
                            onSuccess = { request, result ->
                                userboardMainImageProgressbar.visibility = View.GONE
                            }
                        )
                    }
                }
            }
            userboardMainViews.text = item.views
            userboardMainLikes.text = item.likes

            if (item.isLike) {
                userboardMainLikesButton.setBackgroundResource(R.drawable.paintedheart)
            } else {
                userboardMainLikesButton.setBackgroundResource(R.drawable.heart)
            }

            // item 클릭이벤트
            itemView.setOnClickListener {
                onItemClicked(item, bindingAdapterPosition)
            }

        }
    }
}