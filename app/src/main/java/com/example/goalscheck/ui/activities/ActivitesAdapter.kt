package com.example.goalscheck.ui.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.goalscheck.R
import com.example.goalscheck.database.ActivitiesEntity
import com.example.goalscheck.databinding.AdapterActivityBinding

class ActivitesAdapter(private val context: Context, val clickListener : OnAdapterListener)  :
    RecyclerView.Adapter<ActivitesAdapter.MyViewHolder>() {

    private val activityList = ArrayList<ActivitiesEntity>()
    class MyViewHolder(val binding : AdapterActivityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(activitiesEntity: ActivitiesEntity, clickListener : OnAdapterListener, context: Context) {
            binding.activityname.text = activitiesEntity.activityName
            binding.activitydate.text = activitiesEntity.activityDate
            binding.buttonClick.setOnClickListener {
                binding.checkbox.visibility = View.VISIBLE
                binding.editActivity.visibility = View.VISIBLE
                binding.buttonClose.visibility = View.VISIBLE
            }

            binding.buttonClose.setOnClickListener {
                binding.checkbox.visibility = View.GONE
                binding.editActivity.visibility = View.GONE
                binding.buttonClose.visibility = View.GONE
            }
            if (activitiesEntity.checked==true)  {
                binding.editActivity.visibility = View.GONE
                binding.checkbox.visibility = View.GONE
                binding.buttonClick.visibility = View.GONE
                binding.buttonClose.visibility = View.GONE
                binding.container.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color3))
            }

            binding.checkbox.setOnClickListener {
                activitiesEntity.checked = true
                clickListener.onCheckListener(activitiesEntity)
                binding.editActivity.visibility = View.GONE
                binding.checkbox.visibility = View.GONE
                binding.buttonClick.visibility = View.GONE
                binding.buttonClose.visibility = View.GONE
                binding.container.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color3))
            }

            binding.editActivity.setOnClickListener {
                clickListener.onUpdate(activitiesEntity)
            }
            binding.container.setOnLongClickListener {
                clickListener.onDelete(activitiesEntity)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : AdapterActivityBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_activity, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(activityList[position], clickListener, context)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    fun setList(activitiesEntity: List<ActivitiesEntity>) {
        activityList.clear()
        activityList.addAll(activitiesEntity)
    }

    interface OnAdapterListener{
        fun onDelete(activitiesEntity: ActivitiesEntity)
        fun onUpdate(activitiesEntity: ActivitiesEntity)
        fun onCheckListener(activitiesEntity: ActivitiesEntity)
    }



}