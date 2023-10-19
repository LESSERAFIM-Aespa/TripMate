package kr.sparta.tripmate.ui.scrap

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.R
import kr.sparta.tripmate.data.model.scrap.ScrapModel
import kr.sparta.tripmate.databinding.ScraptitemsBinding
import kr.sparta.tripmate.domain.model.firebase.ScrapEntity
import kr.sparta.tripmate.util.method.removeHtmlTags


class ScrapAdapter(
    private val onItemClick: (ScrapEntity, Int) -> Unit,
    private val onLikeClick: (ScrapEntity, Int) -> Unit
) : ListAdapter<ScrapEntity, ScrapAdapter.ScrapViewHolder>(
    object : DiffUtil.ItemCallback<ScrapEntity>() {
        override fun areItemsTheSame(oldItem: ScrapEntity, newItem: ScrapEntity): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ScrapEntity, newItem: ScrapEntity): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class ScrapViewHolder(private val binding: ScraptitemsBinding) : RecyclerView
    .ViewHolder(binding.root) {
        fun bind(items: ScrapEntity) = with(binding) {
            scrapTitle.text = removeHtmlTags(items.title)
            scrapContent.text = removeHtmlTags(items.description)
            scrapImage.setImageResource(R.drawable.blogimage)

            scrapTypeIconCardView.setOnClickListener {
                onItemClick(items, bindingAdapterPosition)
            }
            scrapLike.setOnClickListener {
                onLikeClick(items, bindingAdapterPosition)
            }
            scrapLike.likeChange(items.isLike)
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
            this.setImageResource(R.drawable.paintedlove)
        } else {
            this.setImageResource(R.drawable.love)
        }
    }
}