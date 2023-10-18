package kr.sparta.tripmate.ui.budget

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.sparta.tripmate.data.model.budget.Category
import kr.sparta.tripmate.databinding.ItemCategoryBinding

class CategoryAdapter(val categoryListEventListener: CategoryListEventListener) :
    ListAdapter<Category, CategoryAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return (oldItem.name == newItem.name) && (oldItem.color == newItem.color)
        }
    }) {

    interface CategoryListEventListener {
        fun onColorButtonClicked(pos: Int, view: Button)
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding) {
            val pos = absoluteAdapterPosition
            val currentItem = currentList[pos]
            if (currentItem.name.isNotBlank()) {
                budgetCategoryNameEdittext.setText(currentItem.name)
            }
            if (currentItem.color.isNotBlank()) {
                budgetCategoryColorButton.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(currentItem.color))
            }
            budgetCategoryColorButton.setOnClickListener {
                categoryListEventListener.onColorButtonClicked(pos, budgetCategoryColorButton)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemCategoryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}