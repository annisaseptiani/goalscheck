package com.example.goalscheck.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.goalscheck.R
import com.example.goalscheck.ui.activities.ActivitesViewModelFactory
import com.example.goalscheck.ui.activities.ActivitiesViewModel
import com.example.goalscheck.ui.goals.GoalsViewModel
import com.example.goalscheck.ui.goals.GoalsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val activitesViewModelFactory : ActivitesViewModelFactory by instance()
    private val goalsViewModelFactory : GoalsViewModelFactory by instance()
    private lateinit var activitiesViewModel: ActivitiesViewModel
    private lateinit var goalsViewModel: GoalsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()

        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        activitiesViewModel = ViewModelProvider(this, activitesViewModelFactory).get(ActivitiesViewModel::class.java)
        goalsViewModel = ViewModelProvider(this, goalsViewModelFactory).get(GoalsViewModel::class.java)
    }

    private fun setupView() {
        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottomnavigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
        val navController = navHostFragment.findNavController()
        bottomNavigation.setupWithNavController(navController)
    }

    private fun setupObserver() {
        activitiesViewModel.titlerBar.observe(this, Observer {
            supportActionBar!!.title = it
        })
        goalsViewModel.titlerBar.observe(this, Observer {
            supportActionBar!!.title = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        return super.onCreateOptionsMenu(menu)
    }


}