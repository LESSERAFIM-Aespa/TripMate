package kr.sparta.tripmate.ui.budget.budgetdetail.statistics

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.databinding.ItemStatisticsBinding

class BudgetDetailStatisticsListAdapter :
    ListAdapter<Pair<Category, String>, BudgetDetailStatisticsListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<Pair<Category, String>>() {
            override fun areItemsTheSame(
                oldItem: Pair<Category, String>,
                newItem: Pair<Category, String>,
            ): Boolean {
                return oldItem.first.num == newItem.first.num
            }

            override fun areContentsTheSame(
                oldItem: Pair<Category, String>,
                newItem: Pair<Category, String>,
            ): Boolean {
                return oldItem.first == newItem.first && oldItem.second == newItem.second
            }

        }
    ) {

    inner class ViewHolder(private val binding: ItemStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.budgetStatisticsCategoryColorView.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(currentList[absoluteAdapterPosition].first.color)
            )
            binding.budgetStatisticsCategoryNameTextview.text =
                currentList[absoluteAdapterPosition].first.name
            binding.budgetStatisticsCategoryDataTextview.text =
                currentList[absoluteAdapterPosition].second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemStatisticsBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}