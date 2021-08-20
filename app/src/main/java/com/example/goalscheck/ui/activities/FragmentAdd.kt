package com.example.goalscheck.ui.activities

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.goalscheck.R
import com.example.goalscheck.database.ActivitiesEntity
import com.example.goalscheck.databinding.FragmentAddBinding
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*

class FragmentAdd : Fragment() {

    private val viewModels by lazy {
        ViewModelProvider(requireActivity()).get(ActivitiesViewModel::class.java)
    }
    private lateinit var binding : FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        binding.viewmodel = viewModels
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTimePicker()
        setupObserver()
    }

    private fun setupObserver(){
        viewModels.message.observe(requireActivity(), {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
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
                activity_time.setText(String.format("%d : %d", hourOfDay, minute))
            }

        }, hour, minute, false)
        activity_time.setOnClickListener {
            timePickerDialog.show()
        }

    }


}