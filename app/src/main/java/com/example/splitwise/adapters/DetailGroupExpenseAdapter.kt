package com.example.splitwise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.model.Split

class DetailGroupExpenseAdapter:
    ListAdapter<Split, DetailGroupExpenseAdapter.ViewHolder>(DiffCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_expense_distribution,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item= getItem(position)
        holder.paidBy.text="${item.userName} owes ${item.amount}"
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val paidBy= itemView.findViewById<TextView>(R.id.paidBy)
    }

    class DiffCallBack(): DiffUtil.ItemCallback<Split>(){
        override fun areItemsTheSame(
            oldItem: Split,
            newItem: Split
        ): Boolean {
            return oldItem.userId==newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: Split,
            newItem: Split
        ): Boolean {
            return oldItem==newItem
        }

    }
}