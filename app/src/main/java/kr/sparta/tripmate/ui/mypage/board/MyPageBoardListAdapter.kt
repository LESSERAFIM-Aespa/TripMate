package kr.sparta.tripmate.ui.mypage.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentMyPageBoardItemBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.util.sharedpreferences.SharedPreferences

class MyPageBoardListAdapter(
    private val onItemClicked: (CommunityEntity) -> Unit,
    private val onLikeClicked: (CommunityEntity) -> Unit
) :
    ListAdapter<CommunityEntity, MyPageBoardListAdapter.Holder>(object : DiffUtil
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
            return oldItem== newItem
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = FragmentMyPageBoardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class Holder(private val binding: FragmentMyPageBoardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommunityEntity) = with(binding) {
            /**
             * 작성자: 서정한
             * 내용: 좋아요버튼 클릭에따른 토글처리
             * */
            fun toggleIsLikeIcon() {
                val uid = SharedPreferences.getUid(itemView.context)
                val isLike = item.likeUsers.find { it == uid } ?: ""

                if (isLike != "") {
                    myPageItemLikesButton.setBackgroundResource(R.drawable.paintedheart)
                } else {
                    myPageItemLikesButton.setBackgroundResource(R.drawable.heart)
                }
            }

            /**
             * 작성자: 서정한
             * 내용: 게시글 이미지 Url을 불러와 표시합니다.
             * */
            fun setBoardImage() {
                // 게시글 이미지
                item.image.let {
                    // 이미지가 없을경우 기본이미지로
                    if(it.isNullOrBlank()) {
                        myPageItemThumbnail.load(R.drawable.emptycommu)
                        return@let
                    }
                    // 이미지가 존재할경우
                    myPageItemThumbnail.load(item.image) {
                        memoryCacheKey(item.image)
                        crossfade(true)
                        listener(
                            onStart = {
                                // 로딩시작
                                myPageItemImageProgressbar.visibility = View.VISIBLE
                            },
                            onSuccess = { request, result ->
                                // 로딩종료
                                myPageItemImageProgressbar.visibility = View.GONE
                            }
                        )

                    }
                }
            }

            /**
             * 작성자: 서정한
             * 내용: 게시글 작성자의 프로필이미지 Url을 불러와 표시합니다.
             * */
            fun setProfileImage() {
                item.profileThumbnail.let {
                    if(it.isNullOrBlank()) {
                        myPageItemProfileThumbnail.load(R.drawable.emptycommu)
                        return@let
                    }

                    myPageItemProfileThumbnail.load(item.profileThumbnail) {
                        memoryCacheKey(item.profileThumbnail)
                        crossfade(true)
                        listener(
                            onStart = {
                                // 로딩시작
                                myPageItemImageProgressbar.visibility = View.VISIBLE
                            },
                            onSuccess = { request, result ->
                                // 로딩종료
                                myPageItemImageProgressbar.visibility = View.GONE
                            }
                        )
                    }
                }
            }

            // 좋아요버튼 업데이트
            toggleIsLikeIcon()
            // 게시글 이미지 불러오기
            setBoardImage()
            // 게시글 작성자 프로필이미지 불러오기
            setProfileImage()

            myPageItemTitle.text = item.title
            myPageItemProfileNickname.text = item.profileNickname
            myPageItemViews.text = item.views.toString()
            myPageItemLikes.text = item.likes.toString()

            // 클릭시 게시글 이동
            itemView.setOnClickListener {
                onItemClicked(item)
            }

            //좋아요 클릭
            myPageItemLikesButton.setOnClickListener {
                onLikeClicked(item)
            }
        }
    }
}