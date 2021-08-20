package com.example.goalscheck.ui.goals

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.goalscheck.R
import com.example.goalscheck.database.ActivitiesEntity
import com.example.goalscheck.database.GoalsEntity
import com.example.goalscheck.databinding.AdapterActivityBinding
import com.example.goalscheck.databinding.AdapterGoalsBinding
import com.example.goalscheck.ui.activities.ActivitesAdapter

class GoalsAdapter (val click : OnAdapterListener)
    : RecyclerView.Adapter<GoalsAdapter.MyViewHolder>(){

    private val goalsList = ArrayList<GoalsEntity>()

    class MyViewHolder(val binding : AdapterGoalsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(goalsEntity: GoalsEntity, clickListener : OnAdapterListener) {
            binding.goalsname.text = goalsEntity.goalsName
            binding.targettime.text = goalsEntity.goalsDatetime
            binding.goalsdescription.text = goalsEntity.goalsDescription

            binding.buttonClick.setOnClickListener {
                binding.goalscheck.visibility = View.VISIBLE
                binding.editgoals.visibility = View.VISIBLE
                binding.buttonClose.visibility = View.VISIBLE
            }
            binding.buttonClose.setOnClickListener {
                binding.goalscheck.visibility = View.GONE
                binding.editgoals.visibility = View.GONE
                binding.buttonClose.visibility = View.GONE
            }
            Log.d("TAG", "bind: " + binding.goalscheck.isChecked)
            if (goalsEntity.goalsCheck == true) {
                binding.goalscheck.visibility = View.GONE
                binding.editgoals.visibility = View.GONE
                binding.buttonClose.visibility = View.GONE
                binding.buttonClick.visibility = View.GONE
                binding.doneBtn.visibility = View.VISIBLE
            }
            binding.goalscheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    goalsEntity.goalsCheck == true
                    clickListener.onCheckListener(goalsEntity)
                    binding.goalscheck.visibility = View.GONE
                    binding.editgoals.visibility = View.GONE
                    binding.buttonClose.visibility = View.GONE
                    binding.buttonClick.visibility = View.GONE
                    binding.doneBtn.visibility = View.VISIBLE
                }
            }

            binding.editgoals.setOnClickListener {
                clickListener.onUpdate(goalsEntity)
            }
            binding.container.setOnLongClickListener {
                clickListener.onDelete(goalsEntity)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : AdapterGoalsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_goals, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(goalsList[position], click)
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    fun setList(list: List<GoalsEntity>) {
        goalsList.clear()
        goalsList.addAll(list)
    }

    interface OnAdapterListener {
        fun onDelete(goalsEntity: GoalsEntity)
        fun onUpdate(goalsEntity: GoalsEntity)
        fun onCheckListener(goalsEntity: GoalsEntity)
    }

}