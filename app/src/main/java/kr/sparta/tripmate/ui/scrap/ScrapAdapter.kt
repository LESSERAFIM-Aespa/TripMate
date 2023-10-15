package kr.sparta.tripmate.ui.scrap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.R
import kr.sparta.tripmate.domain.model.ScrapModel
import kr.sparta.tripmate.databinding.ScraptitemsBinding
import kr.sparta.tripmate.util.method.removeHtmlTags


class ScrapAdapter(
    private val onItemClick: (ScrapModel, Int) -> Unit,
) : ListAdapter<ScrapModel, ScrapAdapter.ScrapViewHolder>(
    object : DiffUtil.ItemCallback<ScrapModel>() {
        override fun areItemsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class ScrapViewHolder(private val binding: ScraptitemsBinding) : RecyclerView
    .ViewHolder(binding.root) {
        fun bind(items: ScrapModel) = with(binding) {
            scrapTitle.text = removeHtmlTags(items.title)
            scrapContent.text = removeHtmlTags(items.description)
            scrapImage.setImageResource(R.drawable.blogimage)

            itemView.setOnClickListener {
                onItemClick(items, bindingAdapterPosition)
            }
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
}