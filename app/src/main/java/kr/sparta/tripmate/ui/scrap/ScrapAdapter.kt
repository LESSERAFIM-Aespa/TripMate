package kr.sparta.tripmate.ui.scrap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.sparta.tripmate.data.model.ScrapModel
import kr.sparta.tripmate.databinding.ScraptitemsBinding
import kr.sparta.tripmate.util.method.removeHtmlTags


class ScrapAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = ArrayList<ScrapModel>()

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    inner class GourmetViewHolder(private val binding: ScraptitemsBinding) : RecyclerView
    .ViewHolder(binding.root) {
        fun bind(items: ScrapModel) {
            binding.apply {
                scrapTitle.text = removeHtmlTags(items.title)
                scrapContent.text = removeHtmlTags(items.description)

                itemView.setOnClickListener {
                    itemClick?.onClick(it, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ScraptitemsBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return GourmetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as GourmetViewHolder
        val items = items[position]
        holder.bind(items)
    }

}