package com.example.splitwise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.splitwise.R
import com.example.splitwise.model.User

class GroupMemberAdapter:
    ListAdapter<User,GroupMemberAdapter.GroupViewHolder>(DiffCallBack()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_member_group_setting, null, false
        )
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GroupViewHolder,
        position: Int
    ) {
        val user = getItem(position)
        holder.userName.text= user.name
        holder.userMail.text= user.email
    }

    class GroupViewHolder(itemView: View): GroupAdapter.ViewHolder(itemView){
        val userName= itemView.findViewById<TextView>(R.id.memberName)
        val userMail= itemView.findViewById<TextView>(R.id.memberMail)
    }
}

class DiffCallBack: DiffUtil.ItemCallback<User>(){
    override fun areItemsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
        return oldItem.id== newItem.id
    }

    override fun areContentsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean {
        return oldItem==newItem
    }
}