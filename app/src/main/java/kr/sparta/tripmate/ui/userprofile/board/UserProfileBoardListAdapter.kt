package kr.sparta.tripmate.ui.userprofile.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.databinding.ItemUserProfileBoardBinding
import kr.sparta.tripmate.ui.userprofile.model.UserProfileModel

class UserProfileBoardListAdapter(

) :
    ListAdapter<UserProfileModel, UserProfileBoardListAdapter.UserProfileHolder>(
        object : DiffUtil.ItemCallback<UserProfileModel>() {
            override fun areItemsTheSame(
                oldItem: UserProfileModel,
                newItem: UserProfileModel
            ): Boolean {
                return oldItem.thumbnail == newItem.thumbnail
            }

            override fun areContentsTheSame(
                oldItem: UserProfileModel,
                newItem: UserProfileModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileHolder {
        val view = ItemUserProfileBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProfileHolder(view)
    }

    override fun onBindViewHolder(holder: UserProfileHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserProfileHolder(private val binding: ItemUserProfileBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item : UserProfileModel)=with(binding) {
                userProfileBoardUserThumbnail.load(item.thumbnail)
                userProfileBoardNickname.text = item.nickname
                userProfileBoardTitle.text = item.title
                userProfileBoardThumbnail.load(item.image)
                userProfileBoardLikes.text = item.likes
                userProfileBoardViews.text = item.views
            }

    }
}