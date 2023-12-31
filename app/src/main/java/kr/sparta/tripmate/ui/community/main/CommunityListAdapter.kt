package kr.sparta.tripmate.ui.community.main

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

class CommunityListAdapter(
    private val onBoardClicked: (CommunityEntity, Int) -> Unit,
    private val onLikeClicked: (CommunityEntity, Int) -> Unit,
    private val onUserProfileClicked: (CommunityEntity, Int) -> Unit,
    private val onItemLongClicked: (CommunityEntity, Int) -> Unit,
    private val getUidFunction : () -> String
) :
    ListAdapter<CommunityEntity, CommunityListAdapter.CommunityHolder>(
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
        fun bind(item: CommunityEntity) = with(binding) {
            /**
             * 작성자: 서정한
             * 내용: url의 이미지가 없을경우 기본이미지로 보여준다.
             * */
            fun setUrlImageOrDefault() {
                item.image?.let {
                    if (it != "") {
                        communityMainThumbnail.load(it) {
                            memoryCacheKey(it)
                            crossfade(true)
                            listener(
                                onStart = {
                                    communityMainImageProgressbar.visibility = View.VISIBLE
                                },
                                onSuccess = { request, result ->
                                    communityMainImageProgressbar.visibility = View.GONE
                                }
                            )
                        }
                    } else {
                        communityMainThumbnail.load(R.drawable.emptycommu)
                    }
                }
            }

            /**
             * 작성자: 서정한
             * 내용: 좋아요버튼 클릭에따른 토글처리
             * */
            fun toggleIsLikeIcon() {
                val uid = getUidFunction()
                val isLike = item.likeUsers.find { it == uid } ?: ""

                if (isLike != "") {
                    communityMainLikesButton.setBackgroundResource(R.drawable.paintedheart)
                } else {
                    communityMainLikesButton.setBackgroundResource(R.drawable.heart)
                }
            }

            toggleIsLikeIcon()
            setUrlImageOrDefault()
            communityMainTitle.text = item.title
            communityMainProfileNickname.text = item.profileNickname
            communityMainViews.text = item.views.toString()
            communityMainLikes.text = item.likes.toString()


            // 좋아요 버튼클릭
            communityMainLikesButton.setOnClickListener {
                onLikeClicked(item, bindingAdapterPosition)
            }

            // 게시물 클릭시 상세페이지로 이동
            itemView.setOnClickListener{
                onBoardClicked(item, bindingAdapterPosition)
            }

            // 게시글 작성유저 클릭시
            communityMainProfileThumbnail.apply {
                load(item.profileThumbnail)
                setOnClickListener {
                    onUserProfileClicked(item, bindingAdapterPosition)
                }
            }

            // 게시물 스크랩
            itemView.setOnLongClickListener {
                onItemLongClicked(item, bindingAdapterPosition)
                true
            }

        }
    }
}