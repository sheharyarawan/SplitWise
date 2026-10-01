package com.example.splitwise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.model.User

class FriendsAdapter: ListAdapter<User, FriendsAdapter.ViewHolder>(DiffCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_friends,
            parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val friend= getItem(position)
        holder.friendName.text= friend.name
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
     val friendName=itemView.findViewById<TextView>(R.id.friendName)

    }
    class DiffCallBack(): DiffUtil.ItemCallback<User>() {
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
}