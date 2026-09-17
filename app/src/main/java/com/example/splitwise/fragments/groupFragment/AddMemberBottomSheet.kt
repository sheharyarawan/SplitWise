package com.example.splitwise.fragments.groupFragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.transition.Visibility
import com.example.splitwise.R
import com.example.splitwise.databinding.CreateAGroupBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddMemberBottomSheet : BottomSheetDialogFragment() {
        private lateinit var binding: CreateAGroupBottomSheetBinding

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
            setTrip()
            setOthers()
            binding.startEditTextTv.setOnClickListener {
                showDatePicker(binding.startEditTextTv)
            }
            binding.endEditTextTv.setOnClickListener {
                showDatePicker(binding.endEditTextTv)
            }
            binding.createGroupToolbar.setNavigationOnClickListener {
                dialog?.dismiss()
            }

        }

    fun setTrip() {

        binding.TripChip.setOnClickListener {
            binding.addTripSwitchCL.visibility = View.VISIBLE
        }

        binding.addTripDateSwitch.setOnCheckedChangeListener { _, isChecked ->

            binding.addTripDatesCL.visibility = if (isChecked) {
                binding.startEditTextTv.hint="Today"
                binding.startEditTextTv.setHintTextColor(Color.GRAY)
                View.VISIBLE

            } else {
                binding.startEditTextTv.setText("")
                binding.endEditTextTv.setText("")
                View.GONE
            }
        }
    }

        fun setOthers(){

            binding.homeChip.setOnClickListener {
                hide()
                Toast.makeText(requireContext(),"Home Clicked", Toast.LENGTH_SHORT).show()
            }
            binding.coupleChip.setOnClickListener {
                hide()
                Toast.makeText(requireContext(),"Couple Clicked", Toast.LENGTH_SHORT).show()
            }
            binding.otherChip.setOnClickListener {
                hide()
                Toast.makeText(requireContext(),"Other Clicked", Toast.LENGTH_SHORT).show()
            }
        }

        fun hide(){
            binding.addTripSwitchCL.visibility= View.GONE
            binding.addTripDatesCL.visibility= View.GONE
            binding.addTripDateSwitch.isChecked=false
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
    }