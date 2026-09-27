package com.example.splitwise.fragments.groupFragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import com.example.splitwise.R
import com.example.splitwise.databinding.CreateAGroupBottomSheetBinding

import com.example.splitwise.viewModels.GroupViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue

class AddMemberBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: CreateAGroupBottomSheetBinding
    private val viewModel: GroupViewModel by viewModels()
    private var selectedGroupType = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateAGroupBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val bottomSheet = dialog?.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        )

        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleGroupTypeClicks()
        handleClicks()
    }

    private fun handleClicks() {

        binding.startEditTextTv.setOnClickListener {
            showDatePicker(binding.startEditTextTv)
        }

        binding.endEditTextTv.setOnClickListener {
            showDatePicker(binding.endEditTextTv)
        }

        binding.createGroupToolbar.setNavigationOnClickListener {
            dialog?.dismiss()
        }

        binding.createGroupToolbar.setOnMenuItemClickListener {
            createGroup()
            true
        }

        binding.addTripDateSwitch.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                binding.addTripDatesCL.visibility = View.VISIBLE
            } else {
                binding.addTripDatesCL.visibility = View.GONE

                binding.startEditTextTv.setText("")
                binding.endEditTextTv.setText("")
            }
        }
    }

    private fun handleGroupTypeClicks() {

        binding.TripChip.setOnClickListener {

            selectedGroupType = "Trip"
            selectChip(binding.TripChip)

            binding.addTripSwitchCL.visibility = View.VISIBLE

        }

        binding.homeChip.setOnClickListener {

            selectedGroupType = "Home"

            hideTripViews()
            selectChip(binding.homeChip)

        }

        binding.coupleChip.setOnClickListener {

            selectedGroupType = "Couple"

            hideTripViews()

            selectChip(binding.coupleChip)
        }

        binding.otherChip.setOnClickListener {

            selectedGroupType = "Other"

            hideTripViews()

            selectChip(binding.otherChip)
        }
    }

    private fun selectChip(selectedChip: TextView) {

        val chips = listOf(
            binding.TripChip,
            binding.homeChip,
            binding.coupleChip,
            binding.otherChip
        )

        chips.forEach { chip ->

            val drawable = chip.background.mutate() as GradientDrawable

            if (chip == selectedChip) {
                drawable.setColor(
                    ContextCompat.getColor(requireContext(), R.color.green)
                )
            } else {
                drawable.setColor(
                    ContextCompat.getColor(requireContext(), R.color.gray)
                )
            }
        }
    }

    private fun hideTripViews() {

        binding.addTripSwitchCL.visibility = View.GONE
        binding.addTripDatesCL.visibility = View.GONE
        binding.addTripDateSwitch.isChecked = false

        binding.startEditTextTv.setText("")
        binding.endEditTextTv.setText("")
    }
    fun showDatePicker(editText: EditText){
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setTheme(R.style.DatePickerTheme)
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = formatter.format(Date(selectedDate))

            editText.setText(date)
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }
    private fun createGroup() {

        val groupName = binding.groupNameEditText.text
            .toString()
            .trim()

        if (groupName.isEmpty()) {

            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.CustomAlertDialog
            )
                .setTitle("Error")
                .setMessage("You haven't entered a name for your group yet!")
                .setPositiveButton("OK", null)
                .show()

            return
        }
        val tripStartDate =
            if (selectedGroupType == "Trip") {
                binding.startEditTextTv.text
                    .toString()
                    .trim()
            } else {
                ""
            }

        val tripEndDate =
            if (selectedGroupType == "Trip") {
                binding.endEditTextTv.text
                    .toString()
                    .trim()
            } else {
                ""
            }

        viewModel.createGroup(

            name = groupName,

            type = selectedGroupType,

            onSuccess = { groupId ->

                parentFragmentManager.setFragmentResult(
                    "group_created",
                    Bundle().apply {
                        putString("groupId", groupId)
                    }
                )

                dialog?.dismiss()
            },

            onFailure = { exception ->

                Toast.makeText(
                    requireContext(),
                    exception.message ?: "Failed to create group",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
}