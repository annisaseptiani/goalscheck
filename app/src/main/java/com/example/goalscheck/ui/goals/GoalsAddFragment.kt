package com.example.goalscheck.ui.goals

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.goalscheck.R
import com.example.goalscheck.databinding.FragmentAddBinding
import com.example.goalscheck.databinding.FragmentGoalsAddBinding
import com.example.goalscheck.ui.activities.ActivitiesViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*


class GoalsAddFragment : DialogFragment(){
    var day = 0
    var month: Int = 0
    var year: Int = 0
    lateinit var cal : Calendar
    private val viewModels by lazy {
        ViewModelProvider(requireActivity()).get(GoalsViewModel::class.java)
    }
    private lateinit var binding : FragmentGoalsAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goals_add, container, false)
        binding.viewmodel = viewModels
        binding.lifecycleOwner = this
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setTimePicker()
        }
        setupObserver()
        return binding.root
    }

    private fun setupObserver(){
        viewModels.message.observe(requireActivity(), {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setTimePicker(){

        val datePickerDialog : DatePickerDialog
        cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

        datePickerDialog = DatePickerDialog(requireActivity(), object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                binding.btnTime.setText(String.format("%d/%d/%d", dayOfMonth, month, year))
            }

        }, day, month, year)

        binding.btnTime.setOnClickListener {
            datePickerDialog.show()
        }
    }

}