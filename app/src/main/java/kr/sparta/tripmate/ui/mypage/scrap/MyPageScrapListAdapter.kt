package kr.sparta.tripmate.ui.mypage.scrap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.api.Constants.COMMUNITYTYPE
import kr.sparta.tripmate.api.Constants.SCRAPTYPE
import kr.sparta.tripmate.databinding.FragmentMypageBookmarkItemBinding
import kr.sparta.tripmate.domain.model.community.CommunityEntity
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.util.ScrapInterface

class MyPageScrapListAdapter(private val onItemClick: (ScrapInterface, Int) -> Unit) :
    ListAdapter<ScrapInterface, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<ScrapInterface>() {
        override fun areItemsTheSame(oldItem: ScrapInterface, newItem: ScrapInterface): Boolean {
            return when {
                oldItem is SearchBlogEntity && newItem is SearchBlogEntity -> {
                    oldItem.link == newItem.link
                }

                oldItem is CommunityEntity && newItem is CommunityEntity -> {
                    oldItem.key == newItem.key
                }

                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: ScrapInterface, newItem: ScrapInterface): Boolean {
            return when {
                oldItem is SearchBlogEntity && newItem is SearchBlogEntity -> {
                    oldItem== newItem
                }

                oldItem is CommunityEntity && newItem is CommunityEntity -> {
                    oldItem == newItem
                }

                else -> false
            }
        }
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SCRAPTYPE -> {
                val view = FragmentMypageBookmarkItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ScrapHolder(view)
            }

            else -> {
                val view = FragmentMypageBookmarkItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CommunityHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ScrapHolder -> {
                val scrapItem = item as SearchBlogEntity
                holder.bind(scrapItem)
            }

            is CommunityHolder -> {
                val communityItem = item as CommunityEntity
                holder.bind(communityItem)
            }
        }
    }

    inner class ScrapHolder(private val binding: FragmentMypageBookmarkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScrapInterface) = with(binding) {
            if (item is SearchBlogEntity) {
                bookmarkImage.setImageResource(R.drawable.blogimage)
                bookmarkTitle.text = item.title
                bookmarkContent.text = item.description
            }

            itemView.setOnClickListener {
                onItemClick(item, bindingAdapterPosition)
            }
        }
    }

    inner class CommunityHolder(private val binding: FragmentMypageBookmarkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScrapInterface) = with(binding) {
            if (item is CommunityEntity) {
                if (item.image.isNullOrBlank()) {
                    bookmarkImage.load(R.drawable.emptycommu) {
                        memoryCacheKey(item.image)
                    }
                } else  {
                    bookmarkImage.load(item.image) {
                        memoryCacheKey(item.image)
                        crossfade(true)
                        listener(
                            onStart = {
                                bookmarkProgressbar.visibility = View.VISIBLE
                            },
                            onSuccess = { request, result ->
                                bookmarkProgressbar.visibility = View.GONE
                            }
                        )
                    }
                }
                bookmarkTitle.text = item.title
                bookmarkContent.text = item.content
                itemView.setOnClickListener {
                    onItemClick(item, bindingAdapterPosition)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is SearchBlogEntity -> SCRAPTYPE
            is CommunityEntity -> COMMUNITYTYPE
            else -> super.getItemViewType(position)
        }
    }

}