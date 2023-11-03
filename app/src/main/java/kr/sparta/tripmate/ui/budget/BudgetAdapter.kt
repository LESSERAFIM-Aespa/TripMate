package kr.sparta.tripmate.ui.budget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.data.model.budget.Budget
import kr.sparta.tripmate.databinding.ItemBudgetBinding
import kr.sparta.tripmate.util.method.toMoneyFormat

class BudgetAdapter(private val budgetListEventListener: BudgetListEventListener) :
    ListAdapter<Budget, BudgetAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.num == newItem.num
        }

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem == newItem && oldItem.resultMoeny == newItem.resultMoeny
        }

    }) {
    interface BudgetListEventListener {
        fun itemClicked(model: Budget)
    }

    inner class ViewHolder(private val binding: ItemBudgetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding) {
            root.setOnClickListener { budgetListEventListener.itemClicked(currentList[absoluteAdapterPosition]) }
            budgetItemStatusBeforeTextview.text =
                "${currentList[absoluteAdapterPosition].money.toMoneyFormat()}원"
            budgetItemStatusAfterTextview.text =
                "${currentList[absoluteAdapterPosition].resultMoeny.toMoneyFormat()}원"
            budgetItemDurationTextview.text =
                "${currentList[absoluteAdapterPosition].startDate} ~ ${currentList[absoluteAdapterPosition].endDate}"
            budgetItemTitleTextview.text = currentList[absoluteAdapterPosition].name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemBudgetBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}