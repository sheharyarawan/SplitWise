package com.example.splitwise.ui.adapters

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.model.Group
import com.example.splitwise.data.model.GroupBalance
import com.example.splitwise.data.model.GroupWithBalance
import com.google.firebase.auth.FirebaseAuth

class GroupAdapter(
    private val onGroupClick: (Group) -> Unit
) : ListAdapter<GroupWithBalance, GroupAdapter.ViewHolder>(DiffCallback()) {

    private val currentUserId =
        FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_groups,
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
        val group = item.group

        holder.name.text = group.name

        holder.itemView.setOnClickListener {
            onGroupClick(group)
        }

        /*
         * People who owe you
         */
        val owedByOthers = item.balances.filter {
            it.toUserId == currentUserId
        }

        /*
         * People you owe
         */
        val youOweOthers = item.balances.filter {
            it.fromUserId == currentUserId
        }

        /*
         * Total group balance
         */
        when {

            item.youGetBack > 0.01 -> {

                holder.groupExpenses.text =
                    createBalanceText(
                        prefix = "You are owed ",
                        amount = item.youGetBack,
                        color = android.R.color.holo_green_light,
                        view = holder.itemView
                    )
            }

            item.youOwe > 0.01 -> {

                holder.groupExpenses.text =
                    createBalanceText(
                        prefix = "You owe ",
                        amount = item.youOwe,
                        color = android.R.color.holo_red_light,
                        view = holder.itemView
                    )
            }

            else -> {

                holder.groupExpenses.text = "Settled"

                holder.groupExpenses.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        android.R.color.white
                    )
                )
            }
        }

        /*
         * People who owe you
         */
        if (owedByOthers.isNotEmpty()) {

            holder.groupOwedBy.visibility = View.VISIBLE

            holder.groupOwedBy.text =
                createPeopleBalanceText(
                    balances = owedByOthers,
                    isOwedToYou = true,
                    view = holder.itemView
                )

        } else {

            holder.groupOwedBy.visibility = View.GONE
            holder.groupOwedBy.text = ""
        }

        /*
         * People you owe
         */
        if (youOweOthers.isNotEmpty()) {

            holder.groupOweTo.visibility = View.VISIBLE

            holder.groupOweTo.text =
                createPeopleBalanceText(
                    balances = youOweOthers,
                    isOwedToYou = false,
                    view = holder.itemView
                )

        } else {

            holder.groupOweTo.visibility = View.GONE
            holder.groupOweTo.text = ""
        }
    }

    /*
     * Creates:
     *
     * You are owed Rs 500
     *
     * Only "Rs 500" is colored.
     */
    private fun createBalanceText(
        prefix: String,
        amount: Double,
        color: Int,
        view: View
    ): SpannableString {

        val amountText = "Rs ${formatAmount(amount)}"

        val text = "$prefix$amountText"

        return SpannableString(text).apply {

            val start = text.indexOf(amountText)

            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        view.context,
                        color
                    )
                ),
                start,
                start + amountText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    /*
     * Creates:
     *
     * Ali owes you Rs 300
     * Ahmed owes you Rs 200
     *
     * OR
     *
     * You owe Bilal Rs 150
     *
     * Only the amounts are colored.
     */
    private fun createPeopleBalanceText(
        balances: List<GroupBalance>,
        isOwedToYou: Boolean,
        view: View
    ): SpannableString {

        val lines = mutableListOf<String>()

        balances.forEach { balance ->

            val line = if (isOwedToYou) {

                "${balance.fromUserName} owes you Rs ${
                    formatAmount(balance.amount)
                }"

            } else {

                "You owe ${balance.toUserName} Rs ${
                    formatAmount(balance.amount)
                }"
            }

            lines.add(line)
        }

        val fullText = lines.joinToString("\n")

        val spannable = SpannableString(fullText)

        val color = if (isOwedToYou) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }

        val actualColor = ContextCompat.getColor(
            view.context,
            color
        )

        /*
         * Find and color each amount.
         */
        var searchStart = 0

        balances.forEach { balance ->

            val amountText =
                "Rs ${formatAmount(balance.amount)}"

            val start =
                fullText.indexOf(
                    amountText,
                    searchStart
                )

            if (start != -1) {

                spannable.setSpan(
                    ForegroundColorSpan(actualColor),
                    start,
                    start + amountText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                searchStart = start + amountText.length
            }
        }

        return spannable
    }

    private fun formatAmount(amount: Double): String {

        return if (amount % 1.0 == 0.0) {

            amount.toInt().toString()

        } else {

            String.format("%.2f", amount)
        }
    }

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val name: TextView =
            itemView.findViewById(R.id.groupName)

        val groupExpenses: TextView =
            itemView.findViewById(R.id.groupExpenses)

        val groupOwedBy: TextView =
            itemView.findViewById(R.id.groupOwedBy)

        val groupOweTo: TextView =
            itemView.findViewById(R.id.groupOweTo)
    }

    class DiffCallback :
        DiffUtil.ItemCallback<GroupWithBalance>() {

        override fun areItemsTheSame(
            oldItem: GroupWithBalance,
            newItem: GroupWithBalance
        ): Boolean {

            return oldItem.group.id == newItem.group.id
        }

        override fun areContentsTheSame(
            oldItem: GroupWithBalance,
            newItem: GroupWithBalance
        ): Boolean {

            return oldItem == newItem
        }
    }
}