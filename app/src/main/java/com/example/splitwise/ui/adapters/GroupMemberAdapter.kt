package com.example.splitwise.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.model.GroupMemberBalance
import kotlin.math.abs

class GroupMemberAdapter :
    ListAdapter<GroupMemberBalance, GroupMemberAdapter.GroupViewHolder>(
        DiffCallBack()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_member_group_setting,
            parent,
            false
        )

        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GroupViewHolder,
        position: Int
    ) {

        val member = getItem(position)

        holder.userName.text = member.name
        holder.userMail.text = member.email

        when {
            member.balance > 0.01 -> {

                holder.memberBalance.text =
                    "gets back\n Rs ${abs(member.balance).toInt()}"

                holder.memberBalance.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green
                    )
                )
            }

            member.balance < -0.01 -> {

                holder.memberBalance.text =
                    "owes \nRs ${abs(member.balance).toInt()}"

                holder.memberBalance.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.red
                    )
                )
            }

            else -> {

                holder.memberBalance.text = ""

//                holder.memberBalance.setTextColor(
//                    ContextCompat.getColor(
//                        holder.itemView.context,
//                        R.color.white
//                    )
//                )
            }
        }
    }

    class GroupViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val userName: TextView =
            itemView.findViewById(R.id.memberName)

        val userMail: TextView =
            itemView.findViewById(R.id.memberMail)

        val memberBalance: TextView =
            itemView.findViewById(R.id.memberBalance)
    }

    class DiffCallBack :
        DiffUtil.ItemCallback<GroupMemberBalance>() {

        override fun areItemsTheSame(
            oldItem: GroupMemberBalance,
            newItem: GroupMemberBalance
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: GroupMemberBalance,
            newItem: GroupMemberBalance
        ): Boolean {
            return oldItem == newItem
        }
    }
}