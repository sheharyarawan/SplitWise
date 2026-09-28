package com.example.splitwise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.model.Expense
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale
class GroupExpensesAdapter(
    val onClick: (Expense) -> Unit
) : ListAdapter<Expense, GroupExpensesAdapter.ViewHolder>(
    DiffCallBack()
) {

    private val currentUserId =
        FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_expense_detail_group,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item = getItem(position)

        val myUserId = currentUserId

        val myShare = item.splits
            .find { it.userId == myUserId }
            ?.amount ?: 0.0


        val myPaidAmount =
            if (item.paidBy == myUserId) {
                item.amount
            } else {
                0.0
            }


        val netAmount = myPaidAmount - myShare

        when {

            netAmount > 0 -> {

                holder.myExpense.text =
                    "you lent\nRs ${netAmount.toInt()}"

                holder.myExpense.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green
                    )
                )
            }

            netAmount < 0 -> {

                holder.myExpense.text =
                    "you borrowed\nRs ${(-netAmount).toInt()}"

                holder.myExpense.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.red
                    )
                )
            }

            else -> {

                holder.myExpense.text = ""

                holder.myExpense.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green
                    )
                )
            }
        }


        holder.expenseDesc.text =
            formatDescription(item.description)

        val formattedName =
            formatName(item.paidByName)

        holder.paidby.text =
            "$formattedName paid Rs ${item.amount}"

        val date = item.createdAt.toDate()

        val month = SimpleDateFormat(
            "MMM",
            Locale.getDefault()
        ).format(date)

        val day = SimpleDateFormat(
            "dd",
            Locale.getDefault()
        ).format(date)

        holder.expenseDate.text =
            "$month\n$day"

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    private fun formatName(name: String): String {

        val parts = name.trim()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }

        if (parts.isEmpty()) {
            return ""
        }

        val firstName = parts[0]
            .lowercase(Locale.getDefault())
            .replaceFirstChar {
                it.uppercase()
            }

        return if (parts.size > 1) {

            val lastInitial = parts.last()
                .first()
                .uppercaseChar()

            "$firstName $lastInitial."

        } else {

            firstName
        }
    }

    private fun formatDescription(
        description: String
    ): String {

        return description
            .trim()
            .lowercase(Locale.getDefault())
            .replaceFirstChar {
                it.uppercase()
            }
    }

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val expenseDesc =
            itemView.findViewById<TextView>(
                R.id.detailGroupExpenseName
            )

        val paidby =
            itemView.findViewById<TextView>(
                R.id.detailGroupExpensePaidBy
            )

        val expenseDate =
            itemView.findViewById<TextView>(
                R.id.expenseDate
            )

        val myExpense =
            itemView.findViewById<TextView>(
                R.id.detailGroupExpenseLent
            )
    }

    class DiffCallBack :
        DiffUtil.ItemCallback<Expense>() {

        override fun areItemsTheSame(
            oldItem: Expense,
            newItem: Expense
        ): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Expense,
            newItem: Expense
        ): Boolean {

            return oldItem == newItem
        }
    }
}

