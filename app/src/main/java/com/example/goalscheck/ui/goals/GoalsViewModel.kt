package com.example.goalscheck.ui.goals

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.goalscheck.R
import com.example.goalscheck.database.ActivitiesEntity
import com.example.goalscheck.database.GoalsEntity
import com.example.goalscheck.repo.Repository
import com.example.goalscheck.util.Event
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

//import com.example.goalscheck.repo.Repository

class GoalsViewModel(
    val repository: Repository
) : ViewModel(){
    val titlerBar : MutableLiveData<String> = MutableLiveData("")
    private lateinit var goalsToUpdate : GoalsEntity
    private var isUpdate = false

    var input_goals = MutableLiveData<String>()
    var input_target = MutableLiveData<String>()
    var input_description = MutableLiveData<String>()
    var isChecked = false
    val saveorUpdateButton = MutableLiveData<String>()

    val goalsList : LiveData<List<GoalsEntity>> = repository.getGoals

    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
        get() = statusMessage

    init {
        saveorUpdateButton.value = "Save"
    }

    fun initUpdate(goalsEntity: GoalsEntity) {
        input_goals.value = goalsEntity.goalsName
        input_target.value = goalsEntity.goalsTarget
        input_description.value = goalsEntity.goalsDescription
        isUpdate = true
        goalsToUpdate = goalsEntity
        saveorUpdateButton.value = "Update"

    }

    fun saveOrUpdateData(view: View) {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentdate = sdf.format(Date())
        val dateCreated = currentdate.toString()
        if (input_goals.value == null || input_target.value == null || input_description.value == null) {
            statusMessage.value = Event("Data Can't Be Empty")
        } else {
            if (isUpdate) {
                goalsToUpdate.goalsName = input_goals.value!!
                goalsToUpdate.goalsTarget = input_target.value!!
                goalsToUpdate.goalsDescription = input_description.value!!
                goalsToUpdate.goalsDatetime = dateCreated
                goalsToUpdate.goalsCheck = false
                Log.d("TAG", "saveOrUpdateData: " + goalsToUpdate)
                updateGoals(goalsToUpdate, view)
            }else {
                val name = input_goals.value!!
                val getTime = input_target.value!!
                val description = input_description.value!!

                insertGoals(GoalsEntity(0, name, getTime, dateCreated, description, isChecked), view)
            }
        }
    }

    private fun insertGoals(goalsEntity: GoalsEntity, view:View) = viewModelScope.launch {
        val newRowId : Long = repository.saveGoals(goalsEntity)
        if (newRowId > -1) {
            statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
            input_goals.value = ""
            input_target.value = ""
            input_description.value = ""
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun deleteGoals(goalsEntity: GoalsEntity) = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteGoals(goalsEntity)
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occured")
        }
    }

    fun updateGoals(goalsEntity: GoalsEntity, view: View) = viewModelScope.launch {
        val noOfRows = repository.updateGoals(goalsEntity)
        if (noOfRows > 0 ) {
            statusMessage.value = Event("$noOfRows Row Updates Successfully")
            input_goals.value = ""
            input_target.value = ""
            input_description.value = ""
        } else {
            statusMessage.value = Event("Error Occured")
        }
    }

}