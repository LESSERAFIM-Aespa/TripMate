package kr.sparta.tripmate.ui.mypage.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import kr.sparta.tripmate.databinding.FragmentMypageBoardItemBinding

class BoardListAdapter:ListAdapter<BoardModel, BoardListAdapter.Holder>(object: DiffUtil.ItemCallback<BoardModel>() {
    override fun areItemsTheSame(oldItem: BoardModel, newItem: BoardModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BoardModel, newItem: BoardModel): Boolean {
        return oldItem == newItem
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = FragmentMypageBoardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        return holder.bind(getItem(position))
    }

    class Holder(private val binding: FragmentMypageBoardItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item : BoardModel)=with(binding) {
            boardThumbnail.load(item.thumbnail){
                transformations(CircleCropTransformation())
            }
            boardTitle.text = item.title
            boardProfileNickname.text = item.profileNickname
            boardProfileThumbnail.load(item.profileThumbnail)
            boardViews.text = item.views
            boardLikes.text = item.likes
        }
    }
}