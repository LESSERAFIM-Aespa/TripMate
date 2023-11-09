package kr.sparta.tripmate.ui.scrap.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ScraptitemsBinding
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.util.method.removeHtmlTags


class ScrapAdapter(
    private val onItemClick: (SearchBlogEntity) -> Unit,
    private val onLikeClick: (SearchBlogEntity, Int) -> Unit
) : ListAdapter<SearchBlogEntity, ScrapAdapter.ScrapViewHolder>(
    object : DiffUtil.ItemCallback<SearchBlogEntity>() {
        override fun areItemsTheSame(oldItem: SearchBlogEntity, newItem: SearchBlogEntity): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: SearchBlogEntity, newItem: SearchBlogEntity): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class ScrapViewHolder(private val binding: ScraptitemsBinding) : RecyclerView
    .ViewHolder(binding.root) {
        fun bind(items: SearchBlogEntity) = with(binding) {
            scrapTitle.text = items.title?.let { removeHtmlTags(it) }
            scrapContent.text = items.description?.let { removeHtmlTags(it) }
            scrapImage.setImageResource(R.drawable.blogimage)

            itemView.setOnClickListener {
                onItemClick(items)
            }

            scrapLike.setOnClickListener {
                onLikeClick(items, bindingAdapterPosition)
            }

            items.isLike.let { scrapLike.likeChange(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder {
        val binding = ScraptitemsBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return ScrapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun ImageView.likeChange(isLiked: Boolean) {
        if (isLiked) {
            this.setImageResource(R.drawable.ic_star_filled)
        } else {
            this.setImageResource(R.drawable.ic_star)
        }
    }
}