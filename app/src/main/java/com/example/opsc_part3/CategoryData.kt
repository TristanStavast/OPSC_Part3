package com.example.opsc_part3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.MutableList

data class CategoryData(
    var username: String? = null,
    var CategoryName: String? = null,
    var Description: String? = null
)

class CategoryAdapter(private var context: Context, private var categoryList: List<CategoryData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerCategoryName: TextView = itemView.findViewById(R.id.headerCategoryName)
        val headerCategoryDescription: TextView = itemView.findViewById(R.id.headerCategoryDescription)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val categoryDescriptionTextView: TextView = itemView.findViewById(R.id.categoryDescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category_header, parent, false)
            HeaderViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
            ItemViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val currentItem = categoryList[position - 1]
            holder.categoryNameTextView.text = currentItem.CategoryName
            holder.categoryDescriptionTextView.text = currentItem.Description
        }
    }

    override fun getItemCount() = categoryList.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    fun updateData(newCategory: List<CategoryData>)
    {
        categoryList = newCategory
        notifyDataSetChanged()
    }
}

