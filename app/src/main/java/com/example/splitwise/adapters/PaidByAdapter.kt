package com.example.splitwise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.model.User

class PaidByAdapter(val onItemSelected:(User)->Unit): ListAdapter<User, PaidByAdapter.PaidByViewHolder>(DiffCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaidByViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_who_paid, parent, false)

        return PaidByViewHolder(view)

    }

    override fun onBindViewHolder(
        holder: PaidByViewHolder,
        position: Int
    ) {
        val user= getItem(position)
        holder.name.text= user.name
        holder.itemView.setOnClickListener {
            onItemSelected(user)
        }
    }

    class PaidByViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name= itemView.findViewById<TextView>(R.id.whoPaidName)
    }
}

class DiffCallBack(): DiffUtil.ItemCallback<User>(){
    override fun areItemsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
       return oldItem==newItem
    }
}