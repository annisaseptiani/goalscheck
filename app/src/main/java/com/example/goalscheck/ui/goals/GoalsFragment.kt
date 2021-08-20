package com.example.goalscheck.ui.goals

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.goalscheck.R
import com.example.goalscheck.database.GoalsEntity
import com.example.goalscheck.databinding.FragmentGoalsBinding
import com.example.goalscheck.ui.activities.PopupClass

class GoalsFragment : Fragment() {
    private val viewModels by lazy {
        ViewModelProvider(requireActivity()).get(GoalsViewModel::class.java)
    }
    private lateinit var binding : FragmentGoalsBinding
    private lateinit var adapter: GoalsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goals, container, false)
        binding.viewmodel = viewModels
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupObserver()
        initRecycleview()
        setOnClickListener()
    }

    private fun setDialog() {
        val popupClass = GoalsAddFragment()
        popupClass.show(childFragmentManager, "GoalsDialog")
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Daily Activities"
    }
    private fun initRecycleview() {
        adapter = GoalsAdapter(object : GoalsAdapter.OnAdapterListener {
            override fun onDelete(goalsEntity: GoalsEntity) {
                val builder = AlertDialog.Builder(requireActivity())
                builder.apply {
                    setTitle("Delete")
                    setMessage("Are you sure delete ${goalsEntity.goalsName}?")
                    setPositiveButton("Delete") {_,_ ->
                        viewModels.deleteGoals(goalsEntity)
                        Toast.makeText(requireActivity(), "Activity Deleted", Toast.LENGTH_SHORT).show()
                        adapter.notifyDataSetChanged()
                    }
                    setNegativeButton("Cancel") {_,_ ->}
                    show()
                }
            }
            override fun onUpdate(goalsEntity: GoalsEntity) {
                viewModels.initUpdate(goalsEntity)
           //     findNavController().navigate(R.id.action_menu_goals_to_goalsAddFragment)
            setDialog()
            }
            override fun onCheckListener(goalsEntity: GoalsEntity) {
                viewModels.isChecked = true
            }
        })
        binding.rvGoals.adapter = adapter
        getGoalsList()

    }

    private fun setOnClickListener() {
        binding.addbtn.setOnClickListener {
    //        findNavController().navigate(R.id.action_menu_goals_to_goalsAddFragment)
        setDialog()
        }
    }

    private fun getGoalsList() {
        viewModels.goalsList.observe(requireActivity(), {
                    adapter.setList(it)
                    adapter.notifyDataSetChanged()
        })
    }
    private fun setupObserver() {
        viewModels.titlerBar.postValue("Goals")
    }
}