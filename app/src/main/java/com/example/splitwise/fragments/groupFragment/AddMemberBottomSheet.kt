package com.example.splitwise.fragments.groupFragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.splitwise.R
import com.example.splitwise.databinding.CreateAGroupBottomSheetBinding
import com.example.splitwise.model.GroupType
import com.example.splitwise.viewModels.GroupViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddMemberBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: CreateAGroupBottomSheetBinding

    private val groupViewModel: GroupViewModel by activityViewModels()

    private var selectedGroupType = GroupType.OTHER

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            CreateAGroupBottomSheetBinding.inflate(
                inflater,
                container,
                false
            )

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val bottomSheet =
            dialog?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )

        bottomSheet?.layoutParams?.height =
            ViewGroup.LayoutParams.MATCH_PARENT

        val behavior =
            BottomSheetBehavior.from(bottomSheet!!)

        behavior.state =
            BottomSheetBehavior.STATE_EXPANDED

        behavior.skipCollapsed = true
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        setTrip()
        setOthers()
        setDatePickers()
        setToolbar()
    }

    private fun setToolbar() {

        binding.createGroupToolbar.setNavigationOnClickListener {
            dismiss()
        }

        binding.createGroupToolbar.setOnMenuItemClickListener { item ->

            when (item.itemId) {

                R.id.createGroup -> {
                    createGroup()
                    true
                }

                else -> false
            }
        }
    }

    private fun setTrip() {

        binding.TripChip.setOnClickListener {

            selectedGroupType =
                GroupType.TRIP

            binding.addTripSwitchCL.visibility =
                View.VISIBLE
        }

        binding.addTripDateSwitch.setOnCheckedChangeListener {
                _,
                isChecked ->

            binding.addTripDatesCL.visibility =
                if (isChecked) {

                    binding.startEditTextTv.hint =
                        "Today"

                    binding.startEditTextTv
                        .setHintTextColor(Color.GRAY)

                    View.VISIBLE

                } else {

                    binding.startEditTextTv.setText("")
                    binding.endEditTextTv.setText("")

                    View.GONE
                }
        }
    }

    private fun setOthers() {

        binding.homeChip.setOnClickListener {

            selectedGroupType =
                GroupType.HOME

            hideTripOptions()

            Toast.makeText(
                requireContext(),
                "Home selected",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.coupleChip.setOnClickListener {

            selectedGroupType =
                GroupType.COUPLE

            hideTripOptions()

            Toast.makeText(
                requireContext(),
                "Couple selected",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.otherChip.setOnClickListener {

            selectedGroupType =
                GroupType.OTHER

            hideTripOptions()

            Toast.makeText(
                requireContext(),
                "Other selected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun hideTripOptions() {

        binding.addTripSwitchCL.visibility =
            View.GONE

        binding.addTripDatesCL.visibility =
            View.GONE

        binding.addTripDateSwitch.isChecked =
            false
    }

    private fun setDatePickers() {

        binding.startEditTextTv.setOnClickListener {

            showDatePicker(
                binding.startEditTextTv
            )
        }

        binding.endEditTextTv.setOnClickListener {

            showDatePicker(
                binding.endEditTextTv
            )
        }
    }

    private fun showDatePicker(
        editText: EditText
    ) {

        val datePicker =
            MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Select date")
                .setTheme(R.style.DatePickerTheme)
                .build()

        datePicker.addOnPositiveButtonClickListener {
                selectedDate ->

            val formatter =
                SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.getDefault()
                )

            val date =
                formatter.format(
                    Date(selectedDate)
                )

            editText.setText(date)
        }

        datePicker.show(
            parentFragmentManager,
            "DATE_PICKER"
        )
    }

    private fun createGroup() {

        val name =
            binding.groupNameEditText
                .text
                .toString()
                .trim()

        groupViewModel.createGroup(
            name = name,
            type = selectedGroupType,

            onSuccess = { groupId ->

                Toast.makeText(
                    requireContext(),
                    "Group created",
                    Toast.LENGTH_SHORT
                ).show()

                dismiss()
            },

            onFailure = { exception ->

                Toast.makeText(
                    requireContext(),
                    exception.message
                        ?: "Failed to create group",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}