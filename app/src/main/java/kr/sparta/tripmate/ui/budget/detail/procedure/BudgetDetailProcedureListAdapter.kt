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
import kr.sparta.tripmate.util.method.toMoneyFormat

/**
 * 작성자: 서정한
 * 내용: 과정 Fragment의 RecyclerView Adapter.
 * */
class BudgetDetailProcedureListAdapter(
    private val onItemClick : (Int) -> Unit,
) :
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
            ),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ProcedureHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProcedureHolder(
        private val binding: ItemProcedureBinding,
        private val onItemClick: (Int) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProcedureModel) = with(binding) {
            budgetProcedureBeforeMoneyTextview.text = item.beforeMoney.toMoneyFormat() + "원"
            budgetProcedurePriceTextview.text = item.price.toMoneyFormat() + "원"
            budgetProcedureTotalAmountTextview.text = item.totalAmount.toMoneyFormat() + "원"
            budgetProcedureTitleTextview.text = item.title
            budgetProcedureTimeTextview.text = item.time
            budgetProcedureCategoryTextview.text = item.categoryName
            budgetProcedureCategoryTextview.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.categoryColor))

            // 세부과정 디테일페이지로 이동
            itemView.setOnClickListener {
                onItemClick(item.num)
            }
        }
    }
}