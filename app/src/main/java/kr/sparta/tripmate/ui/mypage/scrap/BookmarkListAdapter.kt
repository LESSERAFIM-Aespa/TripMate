package kr.sparta.tripmate.ui.mypage.scrap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.FragmentMypageBoardItemBinding
import kr.sparta.tripmate.databinding.FragmentMypageBookmarkItemBinding

class BookmarkListAdapter:ListAdapter<BookmarkModel, BookmarkListAdapter.Holder>(object: DiffUtil.ItemCallback<BookmarkModel>() {
    override fun areItemsTheSame(oldItem: BookmarkModel, newItem: BookmarkModel): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: BookmarkModel, newItem: BookmarkModel): Boolean {
        return oldItem == newItem
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = FragmentMypageBookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(getItem(position))
    }

    class Holder(private val binding: FragmentMypageBookmarkItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item : BookmarkModel)=with(binding) {
            bookmarkImage.setImageResource(R.drawable.blogimage)
            bookmarkTitle.text = item.title
            bookmarkContent.text = item.description
        }
    }
}