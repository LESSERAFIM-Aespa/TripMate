package kr.sparta.tripmate.ui.mypage.scrap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.api.Constants
import kr.sparta.tripmate.api.Constants.COMMUNITYTYPE
import kr.sparta.tripmate.api.Constants.SCRAPTYPE
import kr.sparta.tripmate.databinding.FragmentMypageBookmarkItemBinding
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.domain.model.firebase.CommunityModelEntity
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.util.ScrapInterface

class BookmarkListAdapter(private val onItemClick: (ScrapInterface, Int) -> Unit) :
    ListAdapter<ScrapInterface, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<ScrapInterface>() {
        override fun areItemsTheSame(oldItem: ScrapInterface, newItem: ScrapInterface): Boolean {
            return when {
                oldItem is ScrapEntity && newItem is ScrapEntity -> {
                    oldItem.url == newItem.url
                }
                oldItem is CommunityModelEntity && newItem is CommunityModelEntity -> {
                    oldItem.key == newItem.key
                }
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: ScrapInterface, newItem: ScrapInterface): Boolean {
            return when {
                oldItem is ScrapEntity && newItem is ScrapEntity -> {
                    oldItem.url == newItem.url
                }
                oldItem is CommunityModelEntity && newItem is CommunityModelEntity -> {
                    oldItem.key == newItem.key
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
                val scrapItem = item as ScrapEntity
                holder.bind(scrapItem)
            }
            is CommunityHolder -> {
                val communityItem = item as CommunityModelEntity
                holder.bind(communityItem)
            }
        }
    }

    inner class ScrapHolder(private val binding: FragmentMypageBookmarkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScrapInterface) = with(binding) {
            if (item is ScrapEntity) {
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
            if (item is CommunityModelEntity) {
                if (!item.addedImage.isNullOrEmpty()) {
                    bookmarkImage.load(item.addedImage)
                } else bookmarkImage.setImageResource(R.drawable.emptycommu)
                bookmarkTitle.text = item.title
                bookmarkContent.text = item.description
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is ScrapEntity -> SCRAPTYPE
            is CommunityModelEntity -> COMMUNITYTYPE
            else -> super.getItemViewType(position)
        }
    }

}