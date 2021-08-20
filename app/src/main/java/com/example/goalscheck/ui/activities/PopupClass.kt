package com.example.goalscheck.ui.activities

import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.goalscheck.R
import com.example.goalscheck.databinding.FragmentAddBinding
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*

class PopupClass: DialogFragment() {

    private lateinit var binding : FragmentAddBinding
    private val viewModels by lazy {
        ViewModelProvider(requireActivity()).get(ActivitiesViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        binding.viewmodel = viewModels
        binding.lifecycleOwner = this
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setOnClick()
        setTimePicker()
        setupObserver()
        return binding.root
    }

    private fun setOnClick() {
        binding.buttonCancel.setOnClickListener {
            dialog!!.dismiss()
        }

    }

    private fun setupObserver(){
        viewModels.message.observe(requireActivity(), {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }
        })
    }

    private fun setTimePicker(){
        val timePickerDialog : TimePickerDialog
        val calendar : Calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)

        timePickerDialog = TimePickerDialog(requireActivity(), object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                binding.activityTime.setText(String.format("%d : %d", hourOfDay, minute))
            }

        }, hour, minute, false)
        binding.activityTime.setOnClickListener {
            timePickerDialog.show()
        }
    }


}