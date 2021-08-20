package com.example.goalscheck.ui.activities

import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.goalscheck.R
import com.example.goalscheck.database.ActivitiesEntity
import com.example.goalscheck.repo.Repository
import com.example.goalscheck.util.Event
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ActivitiesViewModel(
    val repository: Repository
) : ViewModel(){
    private var isUpdate = false
    private lateinit var activitiesToUpdate : ActivitiesEntity
    val titlerBar : MutableLiveData<String> = MutableLiveData("")
//    var activitiesListener : ActivitiesListener? = null
    var input_activity = MutableLiveData<String>()
    var input_date = MutableLiveData<String>()
    var dateCreated : String? = null
    var isChecked = false
    val saveorUpdateButton = MutableLiveData<String>()

    val activities : LiveData<List<ActivitiesEntity>> = repository.getActivity

    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
    get() = statusMessage

    init {
        saveorUpdateButton.value = "Save"
    }

    fun initUpdate(activitiesEntity: ActivitiesEntity) {
        input_activity.value = activitiesEntity.activityName
        input_date.value = activitiesEntity.activityDate
        isUpdate = true
        activitiesToUpdate = activitiesEntity
        saveorUpdateButton.value = "Update"

    }

    fun saveOrUpdateData(view: View) {
//        val sdf = SimpleDateFormat("dd/MM/yyyy")
//        val currentdate = sdf.format(Date())
//        dateCreated = currentdate.toString()

        if (input_activity.value == null || input_date.value == null) {
            statusMessage.value = Event("Data Can't Be Empty")
        } else {
            if (isUpdate) {
                activitiesToUpdate.activityName = input_activity.value!!
                activitiesToUpdate.activityDate = input_date.value!!
                activitiesToUpdate.dateCreated
                activitiesToUpdate.checked = false
                Log.d("TAG", "saveOrUpdateData: " + activitiesToUpdate)
                updateActivities(activitiesToUpdate, view)
            }else {
                val nameActivity = input_activity.value!!
                val getTimeActivity = input_date.value!!
                Log.d("ActivitiesViewModel", "onSaveActivity: datecreated" + dateCreated +
                        "activity name: " + nameActivity +
                        "date : " + getTimeActivity)
                insertActivity(ActivitiesEntity(0, nameActivity, getTimeActivity, dateCreated, isChecked), view)
            }
        }
    }

    private fun insertActivity(activitiesEntity: ActivitiesEntity, view:View) = viewModelScope.launch {
        val newRowId : Long = repository.saveActivity(activitiesEntity)
        if (newRowId > -1) {
            statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
            input_activity.value = ""
            input_date.value = ""
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun deleteActivity(activitiesEntity: ActivitiesEntity) = viewModelScope.launch {
     repository.deleteActivities(activitiesEntity)
//        if (noOfRowsDeleted > 0) {
//            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
//        } else {
//            statusMessage.value = Event("Error Occured")
//        }
    }

    fun updateActivities(activitiesEntity: ActivitiesEntity, view: View) = viewModelScope.launch {
        val noOfRows = repository.updateActivities(activitiesEntity)
        if (noOfRows > 0 ) {
            statusMessage.value = Event("$noOfRows Row Updates Successfully")
            input_activity.value = ""
            input_date.value = ""
        } else {
            statusMessage.value = Event("Error Occured")
        }
    }
}
