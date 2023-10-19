package kr.sparta.tripmate.ui.mypage.scrap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentMypageBookmarkItemBinding
import kr.sparta.tripmate.data.model.scrap.ScrapModel

class BookmarkListAdapter(private val onItemClick: (ScrapModel, Int) -> Unit) :
    ListAdapter<ScrapModel,
            BookmarkListAdapter
            .Holder>(object :
        DiffUtil.ItemCallback<ScrapModel>() {
        override fun areItemsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ScrapModel, newItem: ScrapModel): Boolean {
            return oldItem == newItem
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = FragmentMypageBookmarkItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class Holder(private val binding: FragmentMypageBookmarkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScrapModel) = with(binding) {
            bookmarkImage.setImageResource(R.drawable.blogimage)
            bookmarkTitle.text = item.title
            bookmarkContent.text = item.description

            itemView.setOnClickListener {
                onItemClick(item, bindingAdapterPosition)
            }
        }
    }
}