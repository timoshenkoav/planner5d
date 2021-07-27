package com.tunebrains.planner5dtest

import android.app.Application
import com.tunebrains.planner5dtest.data.ProjectsRepository
import com.tunebrains.planner5dtest.ui.ProjectListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PlannerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val repoModule = module {
            single {
                ProjectsRepository()
            }
        }
        val listModule = module {
            viewModel {
                ProjectListViewModel(get())
            }
        }
        startKoin {
            modules(repoModule, listModule)
        }
    }
}