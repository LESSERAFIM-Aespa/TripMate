package kr.sparta.tripmate.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.EmptyViewBinding
import kr.sparta.tripmate.databinding.ScraptitemsBinding
import kr.sparta.tripmate.domain.model.ScrapModel
import kr.sparta.tripmate.util.method.removeHtmlTags

class HomeFirstAdapter(private val onItemClick: (ScrapModel, Int) -> Unit) :
    ListAdapter<ScrapModel,
            RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<ScrapModel>() {
            override fun areItemsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {
                return oldItem == newItem
            }

        }
    ) {

    inner class FirstViewHolder(private val binding: ScraptitemsBinding) : RecyclerView.ViewHolder
        (binding.root) {
        fun bind(items: ScrapModel) = with(binding) {
            Log.d("TripMates","홈 아이템:${items}")
            Log.d("TripMates","홈 아이템:${items.title}")
            Log.d("TripMates", "홈아이템:${items.description}")
            scrapTitle.text = removeHtmlTags(items.title)
            scrapContent.text = removeHtmlTags(items.description)
            scrapImage.setImageResource(R.drawable.blogimage)

            itemView.setOnClickListener {
                onItemClick(items, bindingAdapterPosition)
            }
        }
    }

    inner class SecondViewHolder(private val binding: EmptyViewBinding) : RecyclerView.ViewHolder
        (binding.root) {
        fun bind() {
            binding.emptyTextView.text = "결과가 없습니다."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ScraptitemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FirstViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as FirstViewHolder
        Log.d("TripMates","어댑터 position${position}")
        holder.bind(getItem(position))
    }
}