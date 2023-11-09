package kr.sparta.tripmate.ui.userprofile.board

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
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class UserProfileBoardListAdapter(
    private val onItemClicked: (CommunityEntity, Int) -> Unit,
    private val onLikeClicked: (CommunityEntity, Int) -> Unit
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
                return oldItem == newItem
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
            fun toggleIsLikeIcon() {
                val uid = SharedPreferences.getUid(itemView.context)
                val isLike = item.likeUsers.find { it == uid } ?: ""

                if (isLike != "") {
                    userboardMainLikesButton.setBackgroundResource(R.drawable.paintedheart)
                } else {
                    userboardMainLikesButton.setBackgroundResource(R.drawable.heart)
                }
            }
            userboardMainTitle.text = item.title
            userboardMainProfileNickname.text = item.profileNickname
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
            itemView.setOnClickListener{
                    onItemClicked(item, bindingAdapterPosition)
            }
            userboardMainProfileThumbnail.load(item.profileThumbnail)
            userboardMainViews.text = item.views.toString()
            userboardMainLikes.text = item.likes.toString()
            userboardMainLikesButton.setOnClickListener {
                onLikeClicked(item, bindingAdapterPosition)
            }
            toggleIsLikeIcon()
        }
    }
}