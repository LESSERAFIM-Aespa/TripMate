package kr.sparta.tripmate.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.R
import kr.sparta.tripmate.api.Constants
import kr.sparta.tripmate.databinding.EmptyViewBinding
import kr.sparta.tripmate.databinding.HomeGridviewItemsBinding
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.util.method.removeHtmlTags

// TODO 블로그, 게시글 스크랩으로 구조변경
class HomeScrapListAdapter(private val onItemClick: (SearchBlogEntity, Int) -> Unit) :
    ListAdapter<SearchBlogEntity,
            RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<SearchBlogEntity>() {
            override fun areItemsTheSame(oldItem: SearchBlogEntity, newItem: SearchBlogEntity): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: SearchBlogEntity, newItem: SearchBlogEntity): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    inner class FirstViewHolder(private val binding: HomeGridviewItemsBinding) : RecyclerView.ViewHolder
        (binding.root) {
        fun bind(items: SearchBlogEntity) = with(binding) {
            Log.d("TripMates", "뷰홀더에 항목${items}")
            homeGridTitle.text = removeHtmlTags(items.title)
            homeGridImage.setImageResource(R.drawable.blogimage)

            itemView.setOnClickListener {
                onItemClick(items, bindingAdapterPosition)
            }
        }
    }

    inner class EmptyViewHolder(private val binding: EmptyViewBinding) : RecyclerView.ViewHolder
        (binding.root) {
        fun bind() {
            Log.d("TripMates","빈텍스트 호출되고있냐?")
            binding.emptyTextView.text = "결과가 없습니다."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.EMPTYTYPE -> {
                val binding = EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                EmptyViewHolder(binding)
            }

            else -> {
                val binding =
                    HomeGridviewItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FirstViewHolder(binding)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            Constants.EMPTYTYPE -> {
                holder as EmptyViewHolder
                holder.bind()
            }

            Constants.SCRAPTYPE -> {
                holder as FirstViewHolder
                holder.bind(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is SearchBlogEntity) {
            Constants.SCRAPTYPE
        } else {
            Constants.EMPTYTYPE
        }
    }
}