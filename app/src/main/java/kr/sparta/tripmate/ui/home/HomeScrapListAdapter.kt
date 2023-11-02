package kr.sparta.tripmate.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.sparta.tripmate.R
import kr.sparta.tripmate.api.Constants
import kr.sparta.tripmate.databinding.EmptyViewBinding
import kr.sparta.tripmate.databinding.HomeGridviewItemsBinding
import kr.sparta.tripmate.domain.model.search.SearchBlogEntity
import kr.sparta.tripmate.util.method.removeHtmlTags

class HomeScrapListAdapter(private val onItemClick: (SearchBlogEntity) -> Unit) :
    ListAdapter<SearchBlogEntity,
            RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<SearchBlogEntity>() {
            override fun areItemsTheSame(oldItem: SearchBlogEntity, newItem: SearchBlogEntity): Boolean {
                return oldItem.link == newItem.link
            }

            override fun areContentsTheSame(oldItem: SearchBlogEntity, newItem: SearchBlogEntity): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    inner class BlogViewHolder(private val binding: HomeGridviewItemsBinding) : RecyclerView.ViewHolder
        (binding.root) {
        fun bind(item: SearchBlogEntity) = with(binding) {
            homeGridTitle.text = item.title?.let { removeHtmlTags(it) }
            homeGridImage.load(R.drawable.blogimage){
                memoryCacheKey(item.link)
            }

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    inner class EmptyViewHolder(private val binding: EmptyViewBinding) : RecyclerView.ViewHolder
        (binding.root) {
        fun bind() {
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
                BlogViewHolder(binding)
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
                holder as BlogViewHolder
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