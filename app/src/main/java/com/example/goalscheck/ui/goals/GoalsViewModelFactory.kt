package com.example.goalscheck.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.goalscheck.repo.Repository
import com.example.goalscheck.ui.activities.ActivitiesViewModel

class GoalsViewModelFactory(
    val repository: Repository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GoalsViewModel(repository) as T
    }

}