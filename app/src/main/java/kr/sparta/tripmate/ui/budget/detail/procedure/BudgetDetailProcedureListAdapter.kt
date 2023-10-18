package kr.sparta.tripmate.ui.budget.detail.procedure

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.databinding.ItemProcedureBinding

/**
 * 작성자: 서정한
 * 내용: 과정 Fragment의 RecyclerView Adapter.
 * */
class BudgetDetailProcedureListAdapter :
    ListAdapter<ProcedureModel, BudgetDetailProcedureListAdapter.ProcedureHolder>(
        object : DiffUtil.ItemCallback<ProcedureModel>() {
            override fun areItemsTheSame(
                oldItem: ProcedureModel,
                newItem: ProcedureModel
            ): Boolean {
                return oldItem.num == newItem.num
            }

            override fun areContentsTheSame(
                oldItem: ProcedureModel,
                newItem: ProcedureModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcedureHolder {
        return ProcedureHolder(
            ItemProcedureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProcedureHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProcedureHolder(private val binding: ItemProcedureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProcedureModel) = with(binding) {
            budgetProcedureBeforeMoneyTextview.setText(item.beforeMoney)
            budgetProcedurePriceTextview.setText(item.price)
            budgetProcedureTotalAmountTextview.setText(item.totalAmount)
            budgetProcedureTitleTextview.setText(item.title)
            budgetProcedureTimeTextview.setText(item.time)
            budgetProcedureCategoryTextview.setText(item.categoryName)
            // TODO 카테고리 컬러값 어떻게 구현할지 방법찾아보기!
            // TODO DB에서 가져온 Procedure item들 ProcedureModel에 넣어서 사용하기
        }
    }
}