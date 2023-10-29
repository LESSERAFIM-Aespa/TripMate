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
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.ui.userprofile.model.UserProfileModel

class UserProfileBoardListAdapter(
    private val onItemClicked: (CommunityModelEntity, Int) -> Unit
) :
    ListAdapter<CommunityModelEntity, UserProfileBoardListAdapter.UserProfileHolder>(
        object : DiffUtil.ItemCallback<CommunityModelEntity>() {
            override fun areItemsTheSame(
                oldItem: CommunityModelEntity,
                newItem: CommunityModelEntity
            ): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(
                oldItem: CommunityModelEntity,
                newItem: CommunityModelEntity
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
        fun bind(item: CommunityModelEntity) = with(binding) {
            userboardMainTitle.text = item.title
            userboardMainProfileNickname.text = item.profileNickname
            userboardMainThumbnail.apply {
                if (item.addedImage.isNullOrBlank()) {
                    userboardMainThumbnail.setImageResource(R.drawable.emptycommu)
                } else {
                    userboardMainThumbnail.load(item.addedImage) {
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
                setOnClickListener {
                    onItemClicked(item, bindingAdapterPosition)
                }
            }
            userboardMainViews.text = item.views
            userboardMainLikes.text = item.likes
            if (item.boardIsLike) {
                userboardMainLikesButton.setBackgroundResource(R.drawable.paintedheart)
            } else {
                userboardMainLikesButton.setBackgroundResource(R.drawable.heart)
            }
        }
    }
}