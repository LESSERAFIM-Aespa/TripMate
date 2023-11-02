package kr.sparta.tripmate.ui.budget

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
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
        fun onDeleteButtonClicked(pos: Int)
        fun onColorButtonClicked(pos: Int, button: Button)
    }

    var saveList: MutableList<Category> = mutableListOf()

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding) {
            if (saveList[absoluteAdapterPosition].name.isNotBlank()) {
                budgetCategoryNameEdittext.setText(saveList[absoluteAdapterPosition].name)
            }
            if (saveList[absoluteAdapterPosition].color.isNotBlank()) {
                budgetCategoryColorButton.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(saveList[absoluteAdapterPosition].color))
            }
            budgetCategoryColorButton.setOnClickListener {
                categoryListEventListener.onColorButtonClicked(
                    absoluteAdapterPosition,
                    budgetCategoryColorButton
                )
            }
            budgetCategoryDeleteButton.setOnClickListener {
                categoryListEventListener.onDeleteButtonClicked(absoluteAdapterPosition)
            }
            budgetCategoryNameEdittext.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    saveList[absoluteAdapterPosition] =
                        saveList[absoluteAdapterPosition].copy(name = budgetCategoryNameEdittext.text.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
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