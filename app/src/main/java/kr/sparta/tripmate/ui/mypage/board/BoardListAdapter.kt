package kr.sparta.tripmate.ui.mypage.board

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentCommunityMainItemBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity

class BoardListAdapter(
    private val onItemClicked: (CommunityEntity, Int) -> Unit,
    private val onIsLikeClicked: (CommunityEntity) -> Unit,
    ) :
    ListAdapter<CommunityEntity, BoardListAdapter.Holder>(object : DiffUtil
    .ItemCallback<CommunityEntity>() {
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
        fun bind(item: CommunityEntity) = with(binding) {
            communityMainTitle.text = item.title
            communityMainProfileNickname.text = item.userNickname
            // 프로필 이미지
            communityMainProfileThumbnail.apply {
                if (item.userThumbnail != "") {
                    communityMainProfileThumbnail.load(item.userThumbnail) {
                        listener(
                            onStart = {
                                // 로딩시작
                                communityMainImageProgressbar.visibility = View.VISIBLE
                            },
                            onSuccess = { request, result ->
                                // 로딩종료
                                communityMainImageProgressbar.visibility = View.GONE
                            }
                        )
                    }
                } else if (item.userThumbnail == "") {
                    communityMainProfileThumbnail.load(R.drawable.emptycommu)
                }
            }

            // 게시 이미지
            communityMainThumbnail.apply {
                if (item.image != "") {
                    communityMainThumbnail.load(item.image) {
                        listener(
                            onStart = {
                                // 로딩시작
                                communityMainImageProgressbar.visibility = View.VISIBLE
                            },
                            onSuccess = { request, result ->
                                // 로딩종료
                                communityMainImageProgressbar.visibility = View.GONE
                            }
                        )
                    }
                } else if (item.image == "") {
                    communityMainThumbnail.load(R.drawable.emptycommu)
                }
            }

            communityMainViews.text = item.views
            communityMainLikes.text = item.likes

            if (item.isLike) {
                communityMainLikesButton.setBackgroundResource(R.drawable.paintedheart)
            } else {
                communityMainLikesButton.setBackgroundResource(R.drawable.heart)
            }

            // 게시판 클릭
            itemView.setOnClickListener {
                onItemClicked(item, bindingAdapterPosition)
            }

            //좋아요 클릭
            communityMainLikesButton.setOnClickListener {
                onIsLikeClicked(item)
            }
        }
    }
}