package kr.sparta.tripmate.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.databinding.HomebudgetitemBinding
import kr.sparta.tripmate.util.method.toMoneyFormat

class HomeBudgetListAdapter(private val onItemClicked: (Budget) -> Unit) :
    ListAdapter<Budget, HomeBudgetListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.num == newItem.num
        }

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem == newItem && oldItem.resultMoeny == newItem.resultMoeny
        }

    }) {

    inner class ViewHolder(private val binding: HomebudgetitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item:Budget) = with(binding) {
            homeBudgetItemStatusBeforeTextview.text ="${item.money.toMoneyFormat()}원"
            homeBudgetItemStatusAfterTextview.text = "${item.resultMoeny.toMoneyFormat()}원"
            homeBudgetItemDurationTextview.text = "${item.startDate} ~ ${item.endDate}"
            homeBudgetItemTitleTextview.text = item.name

            itemView.setOnClickListener{
                onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(HomebudgetitemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}