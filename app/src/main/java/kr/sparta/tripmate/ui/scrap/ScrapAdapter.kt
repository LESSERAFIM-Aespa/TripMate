package kr.sparta.tripmate.ui.scrap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.domain.model.ScrapModel
import kr.sparta.tripmate.databinding.ScraptitemsBinding
import kr.sparta.tripmate.util.method.removeHtmlTags


class ScrapAdapter(
    private val onItemClick: (ScrapModel, Int) -> Unit,
) : ListAdapter<ScrapModel, ScrapAdapter.GourmetViewHolder>(
    object : DiffUtil.ItemCallback<ScrapModel>() {
        override fun areItemsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class GourmetViewHolder(private val binding: ScraptitemsBinding) : RecyclerView
    .ViewHolder(binding.root) {
        fun bind(items: ScrapModel) = with(binding) {
            scrapTitle.text = removeHtmlTags(items.title)
            scrapContent.text = removeHtmlTags(items.description)

            itemView.setOnClickListener {
                onItemClick(items, bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GourmetViewHolder {
        val binding = ScraptitemsBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return GourmetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GourmetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}