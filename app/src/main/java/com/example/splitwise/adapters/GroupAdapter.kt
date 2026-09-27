package com.example.splitwise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.model.Group

class GroupAdapter(
    private val onGroupClick: (Group) -> Unit
): ListAdapter<Group,GroupAdapter.ViewHolder>(DiffCallback()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_groups, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val group= getItem(position)
        holder.name.text= group.name
        holder.itemView.setOnClickListener {
            onGroupClick(group)
        }
    }

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name= itemView.findViewById<TextView>(R.id.groupName)
    }
    class DiffCallback : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(
            oldItem: Group,
            newItem: Group
        ): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Group,
            newItem: Group
        ): Boolean {
            return oldItem==newItem
        }
    }
}
