package com.example.goalscheck

import android.app.Application
import com.example.goalscheck.database.DatabaseRoom
import com.example.goalscheck.repo.Repository
import com.example.goalscheck.ui.activities.ActivitesViewModelFactory
import com.example.goalscheck.ui.goals.GoalsViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class BaseApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@BaseApplication))
        bind() from singleton { DatabaseRoom(instance()) }
        bind() from singleton { Repository(instance()) }
        bind() from singleton { ActivitesViewModelFactory(instance()) }
        bind() from singleton { GoalsViewModelFactory(instance()) }
    }
}