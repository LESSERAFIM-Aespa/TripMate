package kr.sparta.tripmate.ui.budget.detail.procedure

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.R
import kr.sparta.tripmate.databinding.ItemProcedureBinding
import kr.sparta.tripmate.util.TripMateApp
import kr.sparta.tripmate.util.method.setCommaForMoneeyText

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
            budgetProcedureBeforeMoneyTextview.text = setCommaForMoneeyText(item.beforeMoney.toString())
            budgetProcedurePriceTextview.text = setCommaForMoneeyText(item.price.toString())
            budgetProcedureTotalAmountTextview.text = setCommaForMoneeyText(item.totalAmount.toString())
            budgetProcedureTitleTextview.text = item.title
            budgetProcedureTimeTextview.text = item.time
            budgetProcedureCategoryTextview.text = item.categoryName
            budgetProcedureCategoryTextview.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.categoryColor))
        }
    }
}