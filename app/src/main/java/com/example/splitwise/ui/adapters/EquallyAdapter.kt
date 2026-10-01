package com.example.splitwise.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.model.User

class EquallyAdapter(
    private val onSelectionChanged: (List<User>) -> Unit
) : ListAdapter<User, EquallyAdapter.ViewHolder>(DiffCallBack()) {

    private val selectedUserIds = mutableSetOf<String>()

    override fun submitList(list: List<User>?) {

        selectedUserIds.clear()

        list?.forEach { user ->
            selectedUserIds.add(user.id)
        }

        super.submitList(list)

        onSelectionChanged(list ?: emptyList())
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_equally,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val user = getItem(position)

        holder.name.text = user.name

        holder.checkBox.isChecked =
            selectedUserIds.contains(user.id)

        holder.itemView.setOnClickListener {
            toggleUser(user)
        }

        holder.checkBox.setOnClickListener {
            toggleUser(user)
        }
    }

    private fun toggleUser(user: User) {

        if (selectedUserIds.contains(user.id)) {
            selectedUserIds.remove(user.id)
        } else {
            selectedUserIds.add(user.id)
        }

        notifyDataSetChanged()

        onSelectionChanged(getSelectedUsers())
    }

    fun selectAll() {

        selectedUserIds.clear()

        currentList.forEach { user ->
            selectedUserIds.add(user.id)
        }

        notifyDataSetChanged()

        onSelectionChanged(getSelectedUsers())
    }

    fun deselectAll() {

        selectedUserIds.clear()

        notifyDataSetChanged()

        onSelectionChanged(getSelectedUsers())
    }

    fun areAllSelected(): Boolean {
        return currentList.isNotEmpty() &&
                selectedUserIds.size == currentList.size
    }

    fun getSelectedUsers(): List<User> {
        return currentList.filter { user ->
            selectedUserIds.contains(user.id)
        }
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val name: TextView =
            itemView.findViewById(R.id.itemEqualName)

        val checkBox: CheckBox =
            itemView.findViewById(R.id.itemEqualCheck)
    }

    class DiffCallBack : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem == newItem
        }
    }
}