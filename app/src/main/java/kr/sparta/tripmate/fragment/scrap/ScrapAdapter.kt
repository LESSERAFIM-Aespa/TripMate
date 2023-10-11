package kr.sparta.tripmate.fragment.scrap

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.sparta.tripmate.api.model.ScrapModel
import kr.sparta.tripmate.databinding.GourmetitemsBinding


class ScrapAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = ArrayList<ScrapModel>()

    inner class GourmetViewHolder(private val binding: GourmetitemsBinding) : RecyclerView
    .ViewHolder(binding.root) {
        fun bind(items: ScrapModel) {
            binding.apply {
                gourmetTitle.text = items.title
                Glide.with(context).load(items.url).into(gourmetUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = GourmetitemsBinding.inflate(
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