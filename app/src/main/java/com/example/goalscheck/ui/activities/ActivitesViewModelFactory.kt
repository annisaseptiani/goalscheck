package com.example.goalscheck.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.goalscheck.repo.Repository

class ActivitesViewModelFactory(
    val repository: Repository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ActivitiesViewModel(repository) as T
    }
}