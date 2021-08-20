package com.example.goalscheck.ui.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.goalscheck.R
import com.example.goalscheck.database.ActivitiesEntity
import com.example.goalscheck.databinding.FragmentActivityBinding
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalendarConfiguration
import com.tejpratapsingh.recyclercalendar.utilities.CalendarUtils
import kotlinx.android.synthetic.main.fragment_activity.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityFragment : Fragment() {

    private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH)
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)

    // current date
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    // selected date
    private var selectedDay: Int = currentDay
    private var selectedMonth: Int = currentMonth
    private var selectedYear: Int = currentYear

    // all days in month
    private val dates = java.util.ArrayList<Date>()

    //horizontal date
    private val date = Date()
    private val startCal = Calendar.getInstance()
    private val endCal = Calendar.getInstance()

    private val viewModels by lazy {
        ViewModelProvider(requireActivity()).get(ActivitiesViewModel::class.java)
    }
    private lateinit var binding : FragmentActivityBinding
    private lateinit var adapter: ActivitesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity, container, false)
        binding.viewmodel = viewModels
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupObserver()
        initRecycleview()
        setCalendar()
        setOnClickListener()
    }

    private fun setCalendar() {
        lastDayInCalendar.add(Calendar.MONTH, 6)
        setUpCalendar()
        binding.calendarPrevButton.setOnClickListener {
            if (cal.after(currentDate)) {
                cal.add(Calendar.MONTH, -1)
                if (cal == currentDate)
                    setUpCalendar()
                else
                    setUpCalendar(changeMonth = cal)
            }
        }
        binding.calendarNextButton.setOnClickListener {
            if (cal.before(lastDayInCalendar)) {
                cal.add(Calendar.MONTH, 1)
                setUpCalendar(changeMonth = cal)
            }
        }

    }

    private fun setUpCalendar(changeMonth: Calendar? = null) {
        txt_current_month!!.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        Log.d("TAG", "setUpCalendar: $selectedDay, $selectedMonth, $selectedYear")

        getListActivity("$selectedDay-$selectedMonth-$selectedYear")
        viewModels.dateCreated = "$selectedDay-$selectedMonth-$selectedYear"

        selectedDay =
            when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }
        selectedMonth =
            when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }
        selectedYear =
            when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }

        var currentPosition = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        while (dates.size < maxDaysInMonth) {
            // get position of selected day
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Assigning calendar view.
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.dateRv!!.layoutManager = layoutManager
        val calendarAdapter = CalendarAdapter(requireActivity(), dates, currentDate, changeMonth)
        binding.dateRv!!.adapter = calendarAdapter

        when {
            currentPosition > 2 -> binding.dateRv!!.scrollToPosition(currentPosition - 3)
            maxDaysInMonth - currentPosition < 2 -> binding.dateRv!!.scrollToPosition(currentPosition)
            else -> binding.dateRv!!.scrollToPosition(currentPosition)
        }

        calendarAdapter.setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]

                getListActivity("$selectedDay-$selectedMonth-$selectedYear")
                viewModels.dateCreated = "$selectedDay-$selectedMonth-$selectedYear"
                Log.d("TAG", "onItemClick: " + selectedDay)
            }
        })
    }

    private fun setupToolbar(){
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Daily Activities"
    }

    private fun setupObserver() {
        viewModels.titlerBar.postValue("Daily Activities")
    }


    private fun setOnClickListener() {
        addnotesbtn.setOnClickListener {
            setDialog()
        }
    }

    private fun setDialog() {
        val popupClass = PopupClass()
        popupClass.show(childFragmentManager, "MyDialog")
    }


    private fun initRecycleview() {
        adapter = ActivitesAdapter(requireContext(), object : ActivitesAdapter.OnAdapterListener {
            override fun onDelete(activitiesEntity: ActivitiesEntity) {
                val builder = AlertDialog.Builder(requireActivity())
                builder.apply {
                    setTitle("Delete")
                    setMessage("Are you sure delete ${activitiesEntity.activityName}?")
                    setPositiveButton("Delete") {_,_ ->
                        viewModels.deleteActivity(activitiesEntity)
                        Toast.makeText(requireActivity(), "Activity Deleted", Toast.LENGTH_SHORT).show()
                        adapter.notifyDataSetChanged()
                    }
                    setNegativeButton("Cancel") {_,_ ->}
                    show()
                }
            }
            override fun onUpdate(activitiesEntity: ActivitiesEntity) {
                viewModels.initUpdate(activitiesEntity)
                setDialog()
                    //         findNavController().navigate(R.id.action_menu_activity_to_fragmentAdd)
            }

            override fun onCheckListener(activitiesEntity: ActivitiesEntity) {
                viewModels.isChecked = true
            }
        })
        binding.rvActivities.adapter = adapter

    }

    private fun getListActivity(dates : String) {
        viewModels.activities.observe(requireActivity(), Observer {
            val activityList = ArrayList<ActivitiesEntity>()
            for (entity in it) {
                if (entity.dateCreated!!.equals(dates)) {
                    activityList.add(entity)
                }
            }
                    adapter.setList(activityList)
                    adapter.notifyDataSetChanged()
        })
    }

}